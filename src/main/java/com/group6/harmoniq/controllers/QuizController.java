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

import com.group6.harmoniq.models.AlbumQuiz;
import com.group6.harmoniq.models.RecognitionQuiz;
import com.group6.harmoniq.models.RecognitionQuizRepository;
import com.group6.harmoniq.models.Track;
import com.group6.harmoniq.models.QuizQuestion;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class QuizController {
    // @Autowired
    // private QuizRepository quizRepository;

    @Autowired
    private RecognitionQuizRepository recognitionQuizRepository;

    // private List<Quiz> allQuizzes;
    private List<AlbumQuiz> allAlbumQuizzes;
    private List<RecognitionQuiz> allRecognitionQuestions;
    private int currentQuestionIndex = 0;
    private int score = 0;  // Add score variable
    private int currentRecognitionQuestionIndex = 0;
    private int recognitionScore = 0;

    @GetMapping("/quizzes/AlbumQuiz/getAll")
    public String getMethodName(HttpSession session, Model model) {
        try{
            model.addAttribute("albumQuizzes", allAlbumQuizzes);
            System.out.println("Get All method called");
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        return "quizzes/allAlbumQuizzes";
    }

    @GetMapping("/quizzes/AlbumQuiz/generate")
    public String generateQuestion(HttpSession session, Model model) {
        List<Track> allTracks = (List<Track>) session.getAttribute("tracks");
        if (allTracks == null) {
            // Handle the case where there are no tracks in the session
            return "quizzes/EmptyTracks"; // Or some other appropriate action
        }
        allAlbumQuizzes.add(createQuizFromTracks(allTracks));
        return "redirect:/quizzes/AlbumQuiz/getAll";
    }

    @GetMapping("/quizzes/AlbumQuiz/start")
    public String startAlbumQuiz(HttpSession session, Model model) {
        return new String();
    }

    public AlbumQuiz createQuizFromTracks(List<Track> tracks) {
        AlbumQuiz quiz = new AlbumQuiz();
        Collections.shuffle(tracks); // Randomize track order
        int numQuestions = 5; // Max 5 questions

        for (int i = 0; i < numQuestions; i++) {
            Track answerTrack = tracks.get(i);
            QuizQuestion question = createQuestion(answerTrack, tracks);
            quiz.getQuestions().add(question); 
        }
        return quiz;
    }

    private QuizQuestion createQuestion(Track answerTrack, List<Track> allTracks) {
        QuizQuestion question = new QuizQuestion();
        question.setQuestionUrl(answerTrack.getAlbumCoverUrl());
        question.setAnswer(answerTrack.getName());

        List<Track> options = new ArrayList<>(allTracks);
        options.remove(answerTrack);
        Collections.shuffle(options);

        question.setOption1(options.get(0).getName());
        question.setOption2(options.get(1).getName());
        question.setOption3(options.get(2).getName());

        return question;
    }

    // @GetMapping("/quizzes/AlbumCoverQuiz/{questionId}")
    // public String getQuiz(@PathVariable Long questionId, Model model) {
    //     if (currentQuestionIndex < allQuizzes.size()) {
    //         Quiz quiz = quizRepository.findById(questionId).orElse(null); // Get the current quiz by Id

    //         // Error handling if no quiz found
    //         if (quiz == null) {
    //             // Handle the case where no quiz is found
    //             return "quizzes/errorPage"; // Redirect to an error page or display a message
    //         }

    //         List<String> options = Arrays.asList(quiz.getAnswer(), quiz.getOption1(), quiz.getOption2(), quiz.getOption3());
    //         Collections.shuffle(options);

    //         model.addAttribute("quiz", quiz);
    //         model.addAttribute("options", options);
    //         model.addAttribute("questionId", quiz.getId());
    //         return "quizzes/AlbumCoverQuiz"; // Redirect to the quiz page
    //     } else {
    //         model.addAttribute("score", score); // Add score to the model for the result page
    //         return "quizzes/quizResult"; // Redirect to result page when quiz is finished
    //     }
    // }
    

    // @PostMapping("/quizzes/AlbumCoverQuiz/submit")
    // public String processAnswer(@RequestParam("selectedOption") String selectedOption, @RequestParam("questionId") Long questionId, Model model) {
    //     Quiz quiz = quizRepository.findById(questionId).orElse(null);

    //     if (quiz != null && selectedOption.equals(quiz.getAnswer())) {
    //         score++; // Increment score for correct answer
    //         model.addAttribute("result", "Correct!");
    //     } else {
    //         model.addAttribute("result", "Incorrect.");
    //     }

    //     currentQuestionIndex++; // Move to the next question

    //     // Redirect to the next quiz question or result page
    //     if (currentQuestionIndex < allQuizzes.size()) {
    //         return "redirect:/quizzes/AlbumCoverQuiz/" + allQuizzes.get(currentQuestionIndex).getId();
    //     } else {
    //         model.addAttribute("score", score);
    //         return "quizzes/quizResult";
    //     }
    // }

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
    public String processRecogntionQuizAnswer(@RequestParam("selectedOption") String selectedOption, @RequestParam("questionId") Long questionId, Model model) {
        RecognitionQuiz recognitionQuestion = recognitionQuizRepository.findById(questionId).orElse(null);

        if (recognitionQuestion != null && selectedOption.equals(recognitionQuestion.getAnswer())) {
            recognitionScore++; // Increment score for correct answer
            model.addAttribute("result", "Correct!");
        } else {
            model.addAttribute("result", "Incorrect.");
        }

        currentRecognitionQuestionIndex++; // Move to the next question

        // Redirect to the next quiz question or result page
        if (currentRecognitionQuestionIndex < allRecognitionQuestions.size()) {
            return "redirect:/quizzes/recognitionQuiz/" + allRecognitionQuestions.get(currentRecognitionQuestionIndex).getId();
        } else {
            model.addAttribute("score", recognitionScore);
            return "quizzes/quizResult";
        }
    }

}
