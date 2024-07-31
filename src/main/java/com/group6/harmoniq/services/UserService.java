package com.group6.harmoniq.services;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.group6.harmoniq.models.User;
import com.group6.harmoniq.models.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // public void incrementUserQuizCount(User user) {
    //     if (user != null) {
    //         user.incrementQuizCount();
    //         userRepository.save(user);
    //     }
    // }
    public void incrementUserQuizCount(User user) {
        if (user != null) {
            User existingUser = userRepository.findBySpotifyId(user.getSpotifyId());
            if (existingUser != null) {
                existingUser.incrementQuizCount();
                userRepository.save(existingUser);
            } else {
                // Handle the case where the user doesn't exist yet (optional)
                System.out.println("User not found in database");
                userRepository.save(user); 
            }
        }
    }

    public void incrementUserAddedSongsLimit(User user) {
        if (user != null) {
            User existingUser = userRepository.findBySpotifyId(user.getSpotifyId());
            if (existingUser != null) {
                existingUser.incrementAddedSongsLimit();
                userRepository.save(existingUser);
            } else {
                // Handle the case where the user doesn't exist yet (optional)
                System.out.println("User not found in database");
                userRepository.save(user); 
            }
        }
    }

    public void updateQuizResults(User user, int correctAnswers, int questions) {
        if (user != null) {
            // Look for the user in the database
            User existingUser = userRepository.findBySpotifyId(user.getSpotifyId());
            if (existingUser != null) {
                // Update quiz results for the existing user
                existingUser.setTotalCorrectAnswers(existingUser.getTotalCorrectAnswers() + correctAnswers);
                existingUser.setTotalQuestions(existingUser.getTotalQuestionsAnswered() + questions);
                updateQuizScoreAverage(existingUser);
                userRepository.save(existingUser);
            } else {
                // Handle the case where the user doesn't exist yet (optional)
                // You might log an error, throw an exception, or create the user.
                System.out.println("User not found in database");
            }
        }
    }


    private void updateQuizScoreAverage(User user) {
        if (user.getTotalQuestionsAnswered() == 0) {
            user.setQuizScoreAverage(0.0);
        } else {
            BigDecimal average = new BigDecimal((double) user.getTotalCorrectAnswers() / user.getTotalQuestionsAnswered() * 100);
            average = average.setScale(2, RoundingMode.HALF_UP);
            user.setQuizScoreAverage(average.doubleValue());
        }
    }
}

