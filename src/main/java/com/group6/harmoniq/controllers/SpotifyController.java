package com.group6.harmoniq.controllers;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import com.group6.harmoniq.models.Artist;
import com.group6.harmoniq.models.Track;
import com.group6.harmoniq.models.User;
import com.group6.harmoniq.models.UserRepository;
import com.group6.harmoniq.services.SpotifyService;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class SpotifyController {

    String client_id;
    String client_secret;
    String redirect_uri;

    String spotifyUrl = "https://accounts.spotify.com";
    final String stateKey = "spotify_auth_state";

    private String accessToken;
    private String refreshToken;
    private final SpotifyService spotifyService;

    public SpotifyController(SpotifyService spotifyService) {
        this.spotifyService = spotifyService;

        this.client_id = this.spotifyService.getClientId();
        this.client_secret = this.spotifyService.getClientSecret();
        this.redirect_uri = this.spotifyService.getRedirectUri();
    }

    public String getAccessToken() {
        return accessToken;
    }

    // redirects to Spotify API's Authorization endpoint
    @GetMapping("/login")
    public String login(HttpServletResponse response) {

        SecureRandom secureRandom;
        try {
            secureRandom = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        String state = generateRandomString(secureRandom, 16);
        Cookie stateCookie = new Cookie(stateKey, state);
        response.addCookie(stateCookie);
        String scope = "user-read-private user-read-email user-follow-read user-top-read playlist-modify-public playlist-modify-private";

        // Requesting Authorization
        String authUrl = UriComponentsBuilder.fromHttpUrl(spotifyUrl + "/authorize")
                .queryParam("response_type", "code")
                .queryParam("client_id", client_id)
                .queryParam("scope", scope)
                .queryParam("redirect_uri", redirect_uri)
                .queryParam("state", state)
                .toUriString();

        return "redirect:" + authUrl;
    }

    @Autowired
    private UserRepository userRepository;
    // Checks application state
    // Requests an access token using user client_id and client_secret
    @GetMapping("/callback")
    public String callback(@RequestParam("code") String code, @RequestParam("state") String state, @CookieValue(stateKey) String storedState, HttpServletResponse response, HttpSession session, Model model) {
     
        if (state == null || !state.equals(storedState)) {
            return "redirect:/index.html";
        } else {

            response.addCookie(new Cookie(stateKey, null));
            String tokenUrl = spotifyUrl + "/api/token";
            String authHeader = "Basic " + Base64.getEncoder().encodeToString((client_id + ":" + client_secret).getBytes(StandardCharsets.UTF_8));

            Map<String, String> bodyParams = new HashMap<>();
            bodyParams.put("code", code);
            bodyParams.put("redirect_uri", redirect_uri);
            bodyParams.put("grant_type", "authorization_code");

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.set("Authorization", authHeader);

            MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
            requestBody.add("code", code);
            requestBody.add("redirect_uri", redirect_uri);
            requestBody.add("grant_type", "authorization_code");

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<Map> responseEntity = restTemplate.postForEntity(tokenUrl, request, Map.class);

            if (responseEntity.getStatusCode() == HttpStatus.OK) {

                try {
                    Map<String, Object> body = responseEntity.getBody();
                    accessToken = (String) body.get("access_token");
                    refreshToken = (String) body.get("refresh_token");
    
                    User user = getUserProfile(accessToken);

                    List<Track> topTracks = getTopTracks(accessToken);
                    List<Artist> topArtists = getTopArtists(accessToken);

                    user.setTopArtist(topArtists.get(0));
                    user.setTopTrack(topTracks.get(0));
                    


                    

                    session.setAttribute("access_token", accessToken);
                    session.setAttribute("refresh_token", refreshToken);

                    session.setAttribute("topTracks", topTracks);
                    session.setAttribute("topArtists", topArtists);

                    model.addAttribute("topTracks", topTracks);
                    model.addAttribute("topArtists", topArtists);


                    session.setAttribute("currentUser", user);
                    session.setAttribute("accessToken", accessToken);


                    model.addAttribute("user", user);
    
                } catch (Exception e) {
                    model.addAttribute("error", e.getMessage());
                }

                return "profile";
            } else {
                return "redirect:/index.html";
            }
        }
    }

    private void saveUserToDatabase(User user) {
        User existingUser = userRepository.findBySpotifyId(user.getSpotifyId());
        if (existingUser == null) {
            userRepository.save(user);
        } else {
            // Update user details if necessary
            existingUser.setDisplayName(user.getDisplayName());
            existingUser.setEmail(user.getEmail());
            existingUser.setFollowers(user.getFollowers());
            existingUser.setImageUrl(user.getImageUrl());
            existingUser.setTopArtist(user.getTopArtist());
            existingUser.setTopTrack(user.getTopTrack());
            userRepository.save(existingUser);
        }
    }

    private String generateRandomString(SecureRandom secureRandom, int length) {
        byte[] randomizedBytes = new byte[length];
        secureRandom.nextBytes(randomizedBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomizedBytes);
    }

    private void getFollowedArtists(String accessToken, Model model) {

        String url = "https://api.spotify.com/v1/me/following?type=artist";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Map.class);
            Map<String, Object> artistsData = (Map<String, Object>) response.getBody().get("artists");
            List<Map<String, Object>> items = (List<Map<String, Object>>) artistsData.get("items");

            model.addAttribute("artists", items);
        } catch (HttpClientErrorException e) {
            model.addAttribute("error", "Failed to get followed artists");
        }
    }

    private User getUserProfile(String accessToken) throws Exception {

        String url = "https://api.spotify.com/v1/me";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Map.class);

            User user = new User();
            user.setDisplayName((String) response.getBody().get("display_name"));
            user.setSpotifyId((String) response.getBody().get("id"));
            user.setEmail((String) response.getBody().get("email"));

            var images = ((List<Map<String, Object>>) response.getBody().get("images"));
            if (images.size() > 0) {
                user.setImageUrl((String) images.get(1).get("url"));
            }

            var followers = (Map<String, Object>) response.getBody().get("followers");
            user.setFollowers((int) followers.get("total"));

            return user;
        } catch (HttpClientErrorException e) {
            throw new Exception("Failed to get user profile", e);
        }
    }

    private List<Artist> getTopArtists(String accessToken) throws Exception {

        // Modify the URL to request top artists (you can adjust the 'limit' parameter)
        String url = "https://api.spotify.com/v1/me/top/artists?limit=10"; 
    
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
    
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
    
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Map.class);
    
            // Extract artist data directly from the response
            List<Map<String, Object>> topArtistsJson = (List<Map<String, Object>>) response.getBody().get("items");
            if (topArtistsJson == null) {
                System.err.println("Error: No 'items' found in Spotify response for top artists.");
                return Collections.emptyList(); 
            }
            
            // Map the JSON data to Artist objects
            List<Artist> topArtists = topArtistsJson.stream()
                .map(artistJson -> {
                    Artist artist = new Artist();
                    artist.setName((String) artistJson.get("name"));
                    
                    // Extract external URLs
                    var externalUrls = (Map<String, Object>) artistJson.get("external_urls");
                    artist.setSpotifyUrl((String) externalUrls.get("spotify"));
    
                    // Extract image URL (if available)
                    var images = (List<Map<String, Object>>) artistJson.get("images");
                    if (images != null && images.size() > 0) {
                        artist.setImageUrl((String) images.get(0).get("url"));
                    }
    
                    return artist;
                })
                .toList(); 
    
            return topArtists;
        } catch (HttpClientErrorException e) {
            throw new Exception("Failed to get top artists", e);
        }
    }

    private List<Track> getTopTracks(String accessToken) throws Exception {

        String url = "https://api.spotify.com/v1/me/top/tracks?limit=10";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Map.class);
            
            List<Map<String, Object>> topTracksJson = (List<Map<String, Object>>) response.getBody().get("items");
            if (topTracksJson == null) {
                System.err.println("Error: No 'items' found in Spotify response.");
                // Handle the error (e.g., return an empty list, throw an exception, etc.)
                return Collections.emptyList(); 
            }
            List<Track> topTracks = topTracksJson.stream()
                .map(trackJson -> {
                    Track track = new Track();
                    track.setName((String) trackJson.get("name"));
                    
                    // Extract artist information
                    var artistsJson = (List<Map<String, Object>>) trackJson.get("artists");
                    var artists = artistsJson.stream().map(artistJson -> {
                        var artist = new Artist();
                        artist.setName((String) artistJson.get("name"));
                        return artist;
                    }).toList();
                    track.setArtists(artists);

                    // Extract album information
                    var album = (Map<String, Object>) trackJson.get("album");
                    var albumCoverArtUrl = ((List<Map<String, Object>>) album.get("images")).get(0).get("url");
                    var albumName = (String) album.get("name");
                    track.setAlbumCoverUrl((String) albumCoverArtUrl);
                    track.setAlbumName(albumName);

                    // Extract Spotify URL
                    var externalUrls = (Map<String, Object>) trackJson.get("external_urls");
                    track.setSpotifyUrl((String) externalUrls.get("spotify"));

                    return track;
                })
                .toList();
        return topTracks;
        } catch (HttpClientErrorException | IndexOutOfBoundsException e) {
            throw new Exception("Failed to get top tracks", e);
        }
    }

    @RequestMapping("/quiz")
    public String quiz(HttpSession session, RedirectAttributes redirectAttributes) {

        String accessToken = (String) session.getAttribute("access_token");
        String refreshToken = (String) session.getAttribute("refresh_token");

        if (accessToken == null) {
            // User hasn't authorized yet. Redirect to initiate the OAuth flow.
            return "redirect:/login"; // Assuming '/authorize' starts the authorization flow
        }

        try {
            List<Track> tracks = getTopTracks(accessToken); 
            session.setAttribute("tracks", tracks);
        } catch (HttpClientErrorException.Unauthorized ex) { // Handle 401 Unauthorized error (token expired)
            if (refreshToken != null) {
                // Attempt to refresh the access token
                accessToken = refreshAccessToken(refreshToken);

                if (accessToken != null) {
                    // Retry the API call with the new access token
                    try {
                        List<Track> tracks = getTopTracks(accessToken);
                        session.setAttribute("tracks", tracks);
                    } catch (Exception e) {
                        // Handle other exceptions if the retry also fails
                        e.printStackTrace();
                    }
                } else {
                    // Token refresh failed, likely due to invalid refresh token or other issue
                    System.out.println("Failed to refresh access token. Please reauthorize.");
                    redirectAttributes.addFlashAttribute("error", "Failed to refresh access token. Please reauthorize.");
                    return "redirect:/login"; // Redirect to re-authorize
                }
            } else {
                // No refresh token available, redirect to authorize
                System.out.println("Session expired. Please reauthorize.");
                redirectAttributes.addFlashAttribute("error", "Session expired. Please reauthorize.");
                return "redirect:/login"; // Redirect to re-authorize
            }
        } catch (Exception e) {
            // Handle other exceptions
            e.printStackTrace();
            // Add an error message to the model or flash attributes
            // (e.g., model.addAttribute("error", "An error occurred while fetching tracks."))
        }

        return "redirect:/quizzes/AlbumQuiz/getAll";
    }

    private String refreshAccessToken(String refreshToken) {
        String tokenUrl = spotifyUrl + "/api/token"; // Same token URL as in your callback
        String authHeader = "Basic " + Base64.getEncoder().encodeToString((client_id + ":" + client_secret).getBytes(StandardCharsets.UTF_8));
    
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", authHeader);
    
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", "refresh_token");
        requestBody.add("refresh_token", refreshToken);
    
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(requestBody, headers);
    
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(tokenUrl, request, Map.class);
    
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> body = responseEntity.getBody();
            String newAccessToken = (String) body.get("access_token");
            // Optionally, update the refresh token if a new one is provided in the response
            if (body.containsKey("refresh_token")) {
                refreshToken = (String) body.get("refresh_token");
            }
            return newAccessToken;
        } else {
            // Handle the error case where token refresh fails
            // (e.g., log an error, return null, throw an exception)
            return null;
        }
    }
}