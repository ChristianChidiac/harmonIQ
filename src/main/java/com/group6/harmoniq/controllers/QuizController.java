package com.group6.harmoniq.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.group6.harmoniq.models.Quiz;
import com.group6.harmoniq.models.QuizRepository;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class QuizController {
    @Autowired
    private QuizRepository quizRepository;

    private List<Quiz> allQuizzes;
    private int currentQuestionIndex = 0;
    private int score = 0;  // Add score variable

    @GetMapping("/quizzes/AlbumCoverQuiz")
    public String startQuiz(Model model) {
        currentQuestionIndex = 0;
        score = 0; // Reset score when starting a new quiz
        allQuizzes = quizRepository.findAll();
        Collections.shuffle(allQuizzes);
        return "redirect:/quizzes/AlbumCoverQuiz/" + allQuizzes.get(currentQuestionIndex).getId(); // Redirect to the first quiz question
    }

    @GetMapping("/quizzes/AlbumCoverQuiz/{questionId}")
    public String getQuiz(@PathVariable Long questionId, Model model) {
        if (currentQuestionIndex < allQuizzes.size()) {
            Quiz quiz = quizRepository.findById(questionId).orElse(null); // Get the current quiz by Id

            // Error handling if no quiz found
            if (quiz == null) {
                // Handle the case where no quiz is found
                return "quizzes/errorPage"; // Redirect to an error page or display a message
            }

            List<String> options = quiz.getOptions();
            Collections.shuffle(options);

            model.addAttribute("quiz", quiz);
            model.addAttribute("options", options);
            model.addAttribute("questionId", quiz.getId());
            return "quizzes/AlbumCoverQuiz"; // Redirect to the quiz page
        } else {
            model.addAttribute("score", score); // Add score to the model for the result page
            return "quizzes/quizResult"; // Redirect to result page when quiz is finished
        }
    }
    

    @PostMapping("/quizzes/AlbumCoverQuiz/submit")
    public String processAnswer(@RequestParam("selectedOption") String selectedOption, @RequestParam("questionId") Long questionId, Model model) {
        Quiz quiz = quizRepository.findById(questionId).orElse(null);

        if (quiz != null && selectedOption.equals(quiz.getAnswer())) {
            score++; // Increment score for correct answer
            model.addAttribute("result", "Correct!");
        } else {
            model.addAttribute("result", "Incorrect.");
        }

        currentQuestionIndex++; // Move to the next question

        // Redirect to the next quiz question or result page
        if (currentQuestionIndex < allQuizzes.size()) {
            return "redirect:/quizzes/AlbumCoverQuiz/" + allQuizzes.get(currentQuestionIndex).getId();
        } else {
            model.addAttribute("score", score);
            return "quizzes/quizResult";
        }
    }
}
