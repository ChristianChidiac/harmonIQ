package com.group6.harmoniq.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.group6.harmoniq.models.User;
import com.group6.harmoniq.models.UserRepository;
import com.group6.harmoniq.services.SpotifyService;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@SpringBootTest(properties = {
 "SPOTIFY_CLIENT_ID=TestClientId",
    "SPOTIFY_CLIENT_SECRET=TestClientSecret",
    "SPOTIFY_REDIRECT_URI=TestRedirectURI"
})
@AutoConfigureMockMvc
public class SpotifyControllerTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RestTemplate restTemplate;

    @Mock
    private HttpServletResponse response;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SpotifyService spotifyService;

    @BeforeAll
    static void loadEnv() {
        Dotenv dotenv = Dotenv.configure().filename(".env.test").load();
        dotenv.entries().forEach(entry -> {
        System.setProperty(entry.getKey(), entry.getValue());
       });
    }



    @Test
    void testLogin() throws Exception {
        
        mockMvc.perform(get("/login"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void testCallbackWithValidState() throws Exception {
        String code = "valid_code";
        String state = "valid_state";
        String authHeader = "Basic " + Base64.getEncoder().encodeToString(("client_id:client_secret").getBytes());      
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("access_token", "access_token");
        
        // Mock access token exchange response
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(Map.class)))
        .thenReturn(new ResponseEntity<>(responseBody, HttpStatus.OK));

        // Mock user profile response
        String userProfileUrl = "https://api.spotify.com/v1/me";
        when(restTemplate.exchange(eq(userProfileUrl), eq(HttpMethod.GET), any(HttpEntity.class), eq(Map.class)))
        .thenReturn(new ResponseEntity<>(userProfileResponse(), HttpStatus.OK));

        // Mock top artist response
        String topArtistUrl = "https://api.spotify.com/v1/me/top/artists?limit=1";
        when(restTemplate.exchange(eq(topArtistUrl), eq(HttpMethod.GET), any(HttpEntity.class), eq(Map.class)))
        .thenReturn(new ResponseEntity<>(topArtistResponse(), HttpStatus.OK));

        // Mock top track response
        String topTrackUrl = "https://api.spotify.com/v1/me/top/tracks?limit=1";
        when(restTemplate.exchange(eq(topTrackUrl), eq(HttpMethod.GET), any(HttpEntity.class), eq(Map.class)))
        .thenReturn(new ResponseEntity<>(topTrackResponse(), HttpStatus.OK));


        when(userRepository.findBySpotifyId(anyString())).thenReturn(null);

        when(userRepository.save(any(User.class))).thenReturn(null);


        mockMvc.perform(get("/callback")
        .param("code", code)
        .param("state", state)
        .cookie(new Cookie("spotify_auth_state", state)))
        .andExpect(status().isOk())
        .andExpect(view().name("profile"));
            }

    @Test
    void testCallbackWithInvalidState() throws Exception {
        mockMvc.perform(get("/callback").param("code", "code").param("state", "invalid_state").cookie(new Cookie("spotify_auth_state", "invalidState")))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/index.html"));
    }

    
    @Test
    void testCallbackWithInvalidResponseEntityStatus() throws Exception {
         
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("access_token", "access_token");
        
        // Mock access token exchange with invalid response
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(Map.class)))
        .thenReturn(new ResponseEntity<>(responseBody, HttpStatus.UNAUTHORIZED));

        mockMvc.perform(get("/callback").param("code", "code").param("state", "invalid_state").cookie(new Cookie("spotify_auth_state", "invalidState")))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/index.html"));
    }

    private Map<String, Object> userProfileResponse() {
        Map<String, Object> response = new HashMap<>();
        response.put("display_name", "HarmonIQ Test User");
        response.put("followers", Map.of("total", 5));
        response.put("images", List.of(Map.of("url", "https://i.scdn.co/image/ab67757000003b82637ec8caf0f0e2542dfddf9a"), Map.of("url", "https://i.scdn.co/image/ab6775700000ee85637ec8caf0f0e2542dfddf9a")));
        return response;
    }

    private Map<String, Object> topArtistResponse() {
        Map<String, Object> artist = new HashMap<>();
        artist.put("name", "Taylor Swift");
        artist.put("external_urls", Map.of("spotify", "https://open.spotify.com/artist/06HL4z0CvFAxyc27GXpf02"));
        artist.put("images", List.of(Map.of("url", "https://i.scdn.co/image/ab6761610000e5ebe672b5f553298dcdccb0e676")));
        
        Map<String, Object> response = new HashMap<>();
        response.put("items", List.of(artist));
        return response;
    }

    private Map<String, Object> topTrackResponse() {
        Map<String, Object> track = new HashMap<>();

        track.put("name", "Bohemian Rhapsody - Remastered 2011");

        Map<String, Object> artist = new HashMap<>();
        artist.put("name", "Queen");
        track.put("artists", List.of(artist));

        track.put("external_urls", Map.of("spotify", "https://open.spotify.com/track/5eIDxmWYxRA0HJBYM9bIIS"));

        Map<String, Object> album = new HashMap<>();
        album.put("name", "A Night At The Opera");
        album.put("images", List.of(Map.of("url", "https://i.scdn.co/image/ab67616d0000b2733025a441495664948b809537")));
        track.put("album", album);
        
        Map<String, Object> response = new HashMap<>();
        response.put("items", List.of(track));
        return response;
    }
}