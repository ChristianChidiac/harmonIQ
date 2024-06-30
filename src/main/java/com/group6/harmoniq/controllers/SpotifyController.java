
// Copyright 2012-2016 Spotify AB

// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at

//     http://www.apache.org/licenses/LICENSE-2.0

// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

//Original Source: https://github.com/spotify/web-api-examples
//This file was modified from the original source by Christian Chidiac on June 30, 2024.
//This code was converted from JavaScript to Java by Christian Chidiac on June 30, 2024.

package com.group6.harmoniq.controllers;

import java.security.SecureRandom;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;


@Controller
public class SpotifyController {
    
    String client_id = System.getenv("SPOTIFY_CLIENT_ID");
    String client_secret = System.getenv("SPOTIFY_CLIENT_SECRET");
    String redirect_uri = "http://localhost:8080/callback";
    final String stateKey = "spotify_auth_state";
    
    
    //redirects to Spotify API's Authorization endpoint
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
        String scope = "user-read-private user-read-email user-follow-read";

        //Requesting Authorization
        String authUrl = UriComponentsBuilder.fromHttpUrl("https://accounts.spotify.com/authorize")
            .queryParam("response_type", "code")
            .queryParam("client_id", client_id)
            .queryParam("scope", scope)
            .queryParam("redirect_uri", redirect_uri)
            .queryParam("state", state)
            .toUriString();

        return "redirect:" + authUrl;
   }
   
    //Checks application state
    //Requests an access token using user client_id and client_secret
    @GetMapping("/callback")
    public String callback(@RequestParam("code") String code, @RequestParam("state") String state, @CookieValue(stateKey) String storedState,
    HttpServletResponse response, Model model) {

        if (state == null || !state.equals(storedState)) {
            return "redirect:/login.html";
        } else {
       
            response.addCookie(new Cookie(stateKey, null));      
            String tokenUrl = "https://accounts.spotify.com/api/token";
            String authHeader = "Basic " + Base64.getEncoder().encodeToString((client_id + ":" + client_secret).getBytes(StandardCharsets.UTF_8));

            Map<String, String> bodyParams = new HashMap<>();
            bodyParams.put("code", code);
            bodyParams.put("redirect_uri", redirect_uri);
            bodyParams.put("grant_type", "authorization_code");
        
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.set("Authorization", authHeader);
       
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("code", code);
            map.add("redirect_uri", redirect_uri);
            map.add("grant_type", "authorization_code");

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

            ResponseEntity<Map> responseEntity = restTemplate.postForEntity(tokenUrl, request, Map.class);

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> body = responseEntity.getBody();
                String access_token = (String) body.get("access_token");
       
            getFollowedArtists(access_token, model);

            return "artists";
            } else {
                return "redirect:/login.html";
            }
        }
    }

    
    private String generateRandomString(SecureRandom secureRandom, int length) {
        byte[] randomizedBytes = new byte[length];
        secureRandom.nextBytes(randomizedBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomizedBytes);
    }


    //Requests followed artists from Spotify API's Followed Artists endpoint, and adds that data to a model
    private void getFollowedArtists(String access_token, Model model) {

        String url = "https://api.spotify.com/v1/me/following?type=artist";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + access_token);

        HttpEntity<String> entity = new HttpEntity<>(headers);
    
        try {

            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
            Map<String, Object> artistsData = (Map<String, Object>) response.getBody().get("artists");
            List<Map<String, Object>> items = (List<Map<String, Object>>) artistsData.get("items");
        
            model.addAttribute("artists", items);
        } catch (HttpClientErrorException e) {
            model.addAttribute("error", "Failed to get followed artists");
        }
    }
}