package com.group6.harmoniq.controllers;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.group6.harmoniq.models.Artist;
import com.group6.harmoniq.models.Track;
import com.group6.harmoniq.models.User;
import com.group6.harmoniq.models.UserRepository;
import com.group6.harmoniq.services.SpotifyService;

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
    private final SpotifyService spotifyService;
   
    private final RestTemplate restTemplate;

    public SpotifyController(SpotifyService spotifyService, RestTemplate restTemplate) {
        this.spotifyService = spotifyService;
        this.restTemplate = restTemplate;

        this.client_id = this.spotifyService.getClientId();
        this.client_secret = this.spotifyService.getClientSecret();
        this.redirect_uri = this.spotifyService.getRedirectUri();
        System.out.println("HERE IS THE CLIENT ID " + client_id);
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
        String scope = "user-read-private user-read-email user-follow-read user-top-read";

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
                    
                    User user = getUserProfile(accessToken);
                    user.setTopArtist(getTopArtist(accessToken));
                    user.setTopTrack(getTopTrack(accessToken));
                   
                    saveUserToDatabase(user);
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
        //RestTemplate restTemplate = new RestTemplate();
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


    private Artist getTopArtist(String accessToken) throws Exception {

        String url = "https://api.spotify.com/v1/me/top/artists?limit=1";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        try {

            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Map.class);
            Map<String, Object> topArtistJson = ((List<Map<String, Object>>) response.getBody().get("items")).get(0);

            var topArtist = new Artist();
            topArtist.setName((String) topArtistJson.get("name"));
            
            var externalUrls = (Map<String, Object>) topArtistJson.get("external_urls");
            topArtist.setSpotifyUrl((String) externalUrls.get("spotify"));
            
            var images = ((List<Map<String, Object>>) topArtistJson.get("images"));
            if (images.size() > 0) {
                topArtist.setImageUrl((String) images.get(0).get("url"));
            }

            return topArtist;
        } catch (HttpClientErrorException e) {
            throw new Exception("Failed to get top artists", e );
        }
    }

    private Track getTopTrack(String accessToken) throws Exception {

        String url = "https://api.spotify.com/v1/me/top/tracks?limit=1";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        try {

            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Map.class);
            
            Map<String, Object> topTrackJson = ((List<Map<String, Object>>) response.getBody().get("items")).get(0);

            var topTrack = new Track();

            topTrack.setName((String) topTrackJson.get("name"));

            var artistsJson = (List<Map<String, Object>>) topTrackJson.get("artists");
            var artists = artistsJson.stream().map(artistJson -> {
                var artist = new Artist();
                artist.setName((String) artistJson.get("name"));
                return artist;
            }).toList();
            topTrack.setArtists(artists);

            var externalUrls = (Map<String, Object>) topTrackJson.get("external_urls");
            topTrack.setSpotifyUrl((String) externalUrls.get("spotify"));

            var album = (Map<String, Object>) topTrackJson.get("album");
            var albumCoverArtUrl = ((List<Map<String, Object>>) album.get("images")).get(0).get("url");
            topTrack.setAlbumCoverUrl((String) albumCoverArtUrl);

            return topTrack;
        } catch (HttpClientErrorException e) {
            throw new Exception("Failed to get top tracks", e);
        }
    }

}