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

    public void incrementUserQuizCount(User user) {
        if (user != null) {
            user.incrementQuizCount();
            userRepository.save(user);
        }
    }

    public void updateQuizResults(User user, int correctAnswers, int questions) {
        if (user != null) {
            user.setTotalCorrectAnswers(user.getTotalCorrectAnswers() + correctAnswers);
            user.setTotalQuestions(user.getTotalQuestionsAnswered() + questions);
            updateQuizScoreAverage(user);
            userRepository.save(user);
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

