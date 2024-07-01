package com.group6.harmoniq.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.group6.harmoniq.models.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {
    @GetMapping("/profile")
    public String getProfile(Model model, HttpServletRequest request, HttpSession session) {
        User user = new User();
        user.setDisplayName("Baljeeeeeeet");
        user.setImageUrl("https://i.scdn.co/image/ab6775700000ee850ff63f304a0cacb691011c34");
        user.setFollowers(10);
        model.addAttribute("user", user);
        return "profile";
    }
    
}
