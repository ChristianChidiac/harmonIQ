package com.group6.harmoniq.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.group6.harmoniq.models.User;
import com.group6.harmoniq.models.UserRepository;
import jakarta.servlet.http.HttpSession;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void incrementUserQuizCount(HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser != null) {
            currentUser.incrementQuizCount();
            userRepository.save(currentUser);
        }
    }
}

