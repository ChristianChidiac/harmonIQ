package com.group6.harmoniq.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.group6.harmoniq.models.User;
import com.group6.harmoniq.models.UserRepository;
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

    @GetMapping("/admin/users")
    public String listAllUsers(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser != null && currentUser.getIsAdmin()) {
            List<User> users = userRepository.findAll();
            model.addAttribute("users", users);
            return "userList";
        } else {
            return "redirect:/accessDenied"; 
        }
    }

    @PostMapping("/admin/setAdmin")
    public String setAdminStatus(@RequestParam("spotifyId") String spotifyId, @RequestParam("isAdmin") boolean isAdmin, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser != null && currentUser.getIsAdmin()) {
            User user = userRepository.findBySpotifyId(spotifyId);
            if (user != null) {
                user.setIsAdmin(isAdmin);
                userRepository.save(user);
            }
            return "redirect:/userList";
        } else {
            return "redirect:/accessDenied"; // Redirect to an access-denied page
        }
    }
    
}
