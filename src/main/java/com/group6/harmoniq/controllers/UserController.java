package com.group6.harmoniq.controllers;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import com.group6.harmoniq.models.User;
import com.group6.harmoniq.models.UserRepository;



@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/callback")
    public String callback(Map<String, String> oauthData) {
        String spotifyId = oauthData.get("spotify_id");
        String displayName = oauthData.get("display_name");
        String email = oauthData.get("email");
        int followers = Integer.parseInt(oauthData.get("followers"));
        String imageUrl = oauthData.get("imageUrl");

        // Check if user already exists
        User user = userRepository.findBySpotifyId(spotifyId);
        if (user == null) {
            user = new User();
            user.setSpotifyId(spotifyId);
            user.setDisplayName(displayName);
            user.setEmail(email);
            user.setFollowers(followers);
            user.setImageUrl(imageUrl);
            userRepository.save(user);
        } else {
            // Update existing user tokens and expiration
            userRepository.save(user);
        }

        // Redirect to the desired page
        return "redirect:/";
    }
}