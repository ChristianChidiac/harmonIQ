package com.group6.harmoniq.controllers;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.group6.harmoniq.controllers.*;
import com.group6.harmoniq.models.Artist;
import com.group6.harmoniq.models.Track;
import com.group6.harmoniq.models.User;
import com.group6.harmoniq.models.UserRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class CollaborativePlaylistController {

    @Autowired
    private SpotifyController controller;

    @Autowired
    private UserRepository userRepository;

    private int index = 0;


@GetMapping("/playlist")
public String showCollaborativePlaylist(HttpSession session, Model model)
{
    //Make playlist_id an environment variable
    String playlist_id = "57E2lj7B6neeSqhqCs51RJ";
    String playlistUrlEndpoint = "https://api.spotify.com/v1/playlists/" + playlist_id;
     
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + (String) session.getAttribute("accessToken"));

    HttpEntity<String> requestEntity = new HttpEntity<>(headers);

    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<Map> response = restTemplate.exchange(playlistUrlEndpoint, HttpMethod.GET, requestEntity, Map.class);

    Map<String, Object> playlistData = response.getBody();

    if (playlistData != null) {

        String playlistName = (String) playlistData.get("name");
        String playlistDescription = (String) playlistData.get("description");
        String playlistUrl = (String) ((Map<String, Object>)playlistData.get("external_urls")).get("spotify");
        List<Map<String, Object>> tracks = (List<Map<String, Object>>) ((Map<String, Object>) playlistData.get("tracks")).get("items");

        model.addAttribute("playlistName", playlistName);
        model.addAttribute("playlistDescription", playlistDescription);
        model.addAttribute("playlistUrl", playlistUrl);
        model.addAttribute("tracks", tracks);

    }

    return "collaborativePlaylist";
}


@GetMapping("/joinPlaylist")
public String joinPlaylist(HttpSession session, Model model)
{
    System.out.println("YOOOOOOOOOOOOOOOOOOO");
    User currentUser = (User) session.getAttribute("currentUser");
    if(currentUser != null)
    {
        User existingUser = userRepository.findBySpotifyId(currentUser.getSpotifyId());
        if(existingUser != null)
        {
            existingUser.setIsCollaborator(true);
            userRepository.save(existingUser);
            System.out.println("YOOOOOOOOOOOOOOOOOOO");
        }
        else
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User is not in database.");
        }
    }
    else
    {
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User session has expired.");
    }
  
    return "collaborativePlaylist";
    }


@GetMapping("/addSongsPopup")
public String showAddSongsPopup(HttpSession session, Model model)
{
    List<Map<String, Object>> top50Tracks = getTop50TracksShuffled(session);
    
    Map<String, Object> track = top50Tracks.get(0);

    model.addAttribute("track", track);

    User currentUser = (User) session.getAttribute("currentUser");
    if(currentUser.getAddedSongs() >= currentUser.getAddedSongsLimit())
    {
        return "/addSongsLimitReached";
    }
    else
    {
    return "addSongs";
    }
}



@PostMapping("/addSong")
public String addSongAndNextSong(@RequestParam("trackUri") String trackUri, HttpSession session, Model model)
{
    //Make playlist_id an environment variable
    String playlist_id = "57E2lj7B6neeSqhqCs51RJ";
    String url = "https://api.spotify.com/v1/playlists/" + playlist_id +"/tracks";

    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + (String) session.getAttribute("accessToken"));

    Map<String, List<String>> body = new HashMap<>();
    body.put("uris", Collections.singletonList(trackUri));

    HttpEntity<Map<String, List<String>>> requestEntity = new HttpEntity<>(body, headers);

    RestTemplate restTemplate = new RestTemplate();

    restTemplate.postForEntity(url, requestEntity, Map.class);

    User currentUser = (User) session.getAttribute("currentUser");
    
    //User with updated addedSongs attribute
    User updatedUser = increaseAddedSongsAttribute(currentUser, session);

    if(updatedUser.getAddedSongs() >= updatedUser.getAddedSongsLimit())
    {
        return "/addSongsLimitReached";
    }

    return addNextSongToModel(session,  model);
}

@GetMapping("/nextSong")
public String nextSong(HttpSession session, Model model)
{
    return addNextSongToModel(session,  model);
}


public String addNextSongToModel(HttpSession session, Model model)
{
    List<Map<String, Object>> top50Tracks = (List<Map<String, Object>>) session.getAttribute("top50Tracks");
    
    //Reset index back to 0 if we've gone through all 50 top tracks
    if(index < top50Tracks.size() - 1)
    {
        index++;
    }
    else
    {
        index = 0;
    }

    Map<String, Object> track = top50Tracks.get(index);
    model.addAttribute("track", track);

    return "addSongs";
}


public  List<Map<String, Object>> getTop50TracksShuffled(HttpSession session)
{
      String getTop50TracksUrl = "https://api.spotify.com/v1/me/top/tracks?limit=50";
    
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + (String) session.getAttribute("accessToken"));

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.exchange(getTop50TracksUrl, HttpMethod.GET, requestEntity, Map.class);
            
        List<Map<String, Object>> top50Tracks = ((List<Map<String, Object>>) response.getBody().get("items"));
        Collections.shuffle(top50Tracks);

        session.setAttribute("top50Tracks", top50Tracks);

        return top50Tracks;
      
}

 private User increaseAddedSongsAttribute(User user, HttpSession session) {
        User existingUser = userRepository.findBySpotifyId(user.getSpotifyId());
        if (existingUser == null) {
               user.setAddedSongs(1);
            userRepository.save(user);
            session.setAttribute("currentUser", user);
            return user;
        } else {
            // Update user details if necessary
            System.out.println(user.getAddedSongs());
            existingUser.setAddedSongs(user.getAddedSongs() + 1);
            userRepository.save(existingUser);
            session.setAttribute("currentUser", existingUser);
            return existingUser;
        }
  
    }

}
