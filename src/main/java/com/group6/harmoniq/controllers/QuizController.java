package com.group6.harmoniq.controllers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.group6.harmoniq.models.Quiz;
import com.group6.harmoniq.models.QuizRepository;
import com.group6.harmoniq.models.RecognitionQuiz;
import com.group6.harmoniq.models.RecognitionQuizRepository;
import com.group6.harmoniq.models.User;
import com.group6.harmoniq.services.UserService;

import jakarta.servlet.http.HttpSession;


@Controller
public class QuizController {
    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private RecognitionQuizRepository recognitionQuizRepository;

    @Autowired
    private UserService userService;

    private List<Quiz> allQuizzes;
    private List<RecognitionQuiz> allRecognitionQuestions;
    private int currentQuestionIndex = 0;
    private int score = 0;  // Add score variable
    private int currentRecognitionQuestionIndex = 0;
    private int recognitionScore = 0;
    private User currentUser; // Declare user as an attribute of the controller

    private void setCurrentUser(HttpSession session) {
        this.currentUser = (User) session.getAttribute("currentUser");
    }


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

            // Error handling if no quiz or user found
            if (quiz == null) {
                // Handle the case where no quiz is found
                return "quizzes/errorPage"; // Redirect to an error page or display a message
            }

            List<String> options = Arrays.asList(quiz.getAnswer(), quiz.getOption1(), quiz.getOption2(), quiz.getOption3());
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
    public String processAnswer(@RequestParam("selectedOption") String selectedOption, @RequestParam("questionId") Long questionId, Model model, HttpSession session) {
        setCurrentUser(session); // Set the current user

        Quiz quiz = quizRepository.findById(questionId).orElse(null);

        if (quiz != null && currentUser != null) {
            int questionScore = selectedOption.equals(quiz.getAnswer()) ? 1 : 0; // Assign score based on correctness
            score += questionScore; // Increment score based on correctness
            userService.updateQuizResults(currentUser, questionScore, 1); // Update quiz result for user
            if (questionScore == 1) {
                model.addAttribute("result", "Correct!");
            } else {
                model.addAttribute("result", "Incorrect.");
            }
        }

        currentQuestionIndex++; // Move to the next question

        // Redirect to the next quiz question or result page
        if (currentQuestionIndex < allQuizzes.size()) {
            return "redirect:/quizzes/AlbumCoverQuiz/" + allQuizzes.get(currentQuestionIndex).getId();
        } else {
            userService.incrementUserQuizCount(currentUser); // Increment user quiz count at the end of the quiz
            model.addAttribute("score", score);
            return "quizzes/quizResult";
        }
    }

    @GetMapping("/quizzes/recognitionQuiz")
    public String startRecognitionQuiz(Model model) {
        currentRecognitionQuestionIndex = 0;
        recognitionScore = 0; // Reset score when starting a new quiz
        allRecognitionQuestions = recognitionQuizRepository.findAll();
        Collections.shuffle(allRecognitionQuestions);
        return "redirect:/quizzes/recognitionQuiz/" + allRecognitionQuestions.get(currentRecognitionQuestionIndex).getId(); // Go to the first quiz question
    }

    @GetMapping("/quizzes/recognitionQuiz/{questionId}")
    public String getRecognitionQuizQuestion(@PathVariable Long questionId, Model model) {
        if (currentRecognitionQuestionIndex < allRecognitionQuestions.size()) {
            RecognitionQuiz recognitionQuestion = recognitionQuizRepository.findById(questionId).orElse(null); // Get the current quiz by Id

            // directs to errorPage template if no question is found
            if (recognitionQuestion == null) {
                return "quizzes/errorPage"; 
            }

            List<String> options = Arrays.asList(recognitionQuestion.getAnswer(), recognitionQuestion.getOption1(), recognitionQuestion.getOption2(), recognitionQuestion.getOption3());
            Collections.shuffle(options);

            model.addAttribute("preview", recognitionQuestion.getPreviewUrl());
            model.addAttribute("options", options);
            model.addAttribute("questionId", recognitionQuestion.getId());
            return "quizzes/recognitionQuiz"; // Redirect to the quiz page
        } else {
            model.addAttribute("score", recognitionScore); // Add score to the model for the result page
            return "quizzes/quizResult"; // Redirect to result page when quiz is finished
        }
    }
    
    @PostMapping("/quizzes/recognitionQuiz/submit")
    public String processRecogntionQuizAnswer(@RequestParam("selectedOption") String selectedOption, @RequestParam("questionId") Long questionId, Model model, HttpSession session) {
        setCurrentUser(session);
        RecognitionQuiz recognitionQuestion = recognitionQuizRepository.findById(questionId).orElse(null);

        if (recognitionQuestion != null && currentUser != null) {
            int questionScore = selectedOption.equals(recognitionQuestion.getAnswer()) ? 1 : 0; // Assign score based on correctness
            recognitionScore += questionScore; // Increment score based on correctness
            userService.updateQuizResults(currentUser, questionScore, 1); // Update quiz result for user
            if (questionScore == 1) {
                model.addAttribute("result", "Correct!");
            } else {
                model.addAttribute("result", "Incorrect.");
            }
        }

        currentRecognitionQuestionIndex++; // Move to the next question

        // Redirect to the next quiz question or result page
        if (currentRecognitionQuestionIndex < allRecognitionQuestions.size()) {
            return "redirect:/quizzes/recognitionQuiz/" + allRecognitionQuestions.get(currentRecognitionQuestionIndex).getId();
        } else {
            userService.incrementUserQuizCount(currentUser); // Increment user quiz count at the end of the quiz
            model.addAttribute("score", recognitionScore);
            return "quizzes/quizResult";
        }
    }

}
