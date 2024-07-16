package com.group6.harmoniq.controllers;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.group6.harmoniq.models.User;
import com.group6.harmoniq.repositories.UserRepository;
import com.group6.harmoniq.services.SpotifyService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    private final SpotifyService spotifyService;

    public UserController(SpotifyService spotifyService) {
        this.spotifyService = spotifyService;
    }


    @GetMapping("/add")
    public String callback(Map<String, String> oauthData) {
        String spotifyId = oauthData.get("spotify_id");
        String displayName = oauthData.get("display_name");
        String email = oauthData.get("email");
        int followers = Integer.parseInt(oauthData.get("followers"));
        String imageUrl = oauthData.get("imageUrl");

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
            userRepository.save(user);
        }

        return "redirect:/";
    }

    @GetMapping("/profile")
    public String getProfile(Model model, HttpServletRequest request, HttpSession session) {
        User user = new User();
        user.setDisplayName("Baljeeeeeeet");
        user.setImageUrl("https://i.scdn.co/image/ab6775700000ee850ff63f304a0cacb691011c34");
        user.setFollowers(10);
        user.setTopArtist(spotifyService.getTopArtist());
        user.setTopTrack(spotifyService.getTopTrack());

        model.addAttribute("user", user);

        return "profile";
    }
}