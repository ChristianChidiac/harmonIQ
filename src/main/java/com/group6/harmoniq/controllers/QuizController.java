package com.group6.harmoniq.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.group6.harmoniq.models.Quiz;
import com.group6.harmoniq.models.QuizRepository;

import com.group6.harmoniq.models.RecognitionQuiz;
import com.group6.harmoniq.models.RecognitionQuizRepository;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;


@Controller
public class QuizController {
    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private RecognitionQuizRepository recognitionQuizRepository;

    private List<Quiz> allQuizzes;
    // private List<Map<String, Object>> allRecognitionQuestions;
    List<Map<String, Object>> recognitionQuizAnswers = new ArrayList<>();
    List<Map<String, Object>> recognitionQuizOptions = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private int score = 0;  // Add score variable
    private int currentRecognitionQuestionIndex = 0;
    private int recognitionScore = 0;
    

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

    @GetMapping("/quizzes/recognitionQuiz")
    public String startRecognitionQuiz(Model model, HttpSession session) {

        currentRecognitionQuestionIndex = 0;
        recognitionScore = 0; // Reset score when starting a new quiz
        addRecognitionQuizAnswersAndOptionsFromTop50Tracks(session);

        return "redirect:/quizzes/recognitionQuiz/" + currentRecognitionQuestionIndex; // Go to the first quiz question
    }

    @GetMapping("/quizzes/recognitionQuiz/{questionId}")
    public String getRecognitionQuizQuestion(@PathVariable int questionId, Model model) {
        if (currentRecognitionQuestionIndex < recognitionQuizAnswers.size()) {
         
           
            Map<String,Object> answer = recognitionQuizAnswers.get(questionId);
             //When questionId = 0, we add the recognitionQuizOptions at indices 0,1, and 2 to the model.  When questionId = 1,
             // we add the recognitionQuizOptions at indices 3,4, and 5 to the model, and so on
            List<Map<String,Object>> options = Arrays.asList(answer, recognitionQuizOptions.get(questionId * 3),  recognitionQuizOptions.get((questionId * 3) + 1),  recognitionQuizOptions.get((questionId * 3) + 2));
            Collections.shuffle(options);

            model.addAttribute("answer", answer);
            model.addAttribute("options", options);
            model.addAttribute("questionId", questionId);
            return "quizzes/recognitionQuiz"; // Redirect to the quiz page
        } else {
            model.addAttribute("score", recognitionScore); // Add score to the model for the result page
            return "quizzes/quizResult"; // Redirect to result page when quiz is finished
        }
    }
    
    @PostMapping("/quizzes/recognitionQuiz/submit")
    public String processRecogntionQuizAnswer(@RequestParam("selectedOption") String selectedOption, @RequestParam("questionId") int questionId, Model model) {
    System.out.println(recognitionQuizAnswers.size());
        if (recognitionQuizAnswers != null && selectedOption.equals((recognitionQuizAnswers.get(questionId)).get("name"))) {
            recognitionScore++; // Increment score for correct answer
            model.addAttribute("result", "Correct!");
        } else {
            model.addAttribute("result", "Incorrect.");
        }

        currentRecognitionQuestionIndex++; // Move to the next question

        // Redirect to the next quiz question or result page
        if (currentRecognitionQuestionIndex < recognitionQuizAnswers.size()) {
            return "redirect:/quizzes/recognitionQuiz/" + currentRecognitionQuestionIndex;
        } else {
            model.addAttribute("score", recognitionScore);
            return "quizzes/quizResult";
        }  

    }


    public void addRecognitionQuizAnswersAndOptionsFromTop50Tracks(HttpSession session)
    {
          String getTop50TracksUrl = "https://api.spotify.com/v1/me/top/tracks?limit=50";
        
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + (String) session.getAttribute("accessToken"));
    
            HttpEntity<String> requestEntity = new HttpEntity<>(headers);
    
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map> response = restTemplate.exchange(getTop50TracksUrl, HttpMethod.GET, requestEntity, Map.class);
                
            List<Map<String, Object>> top50Tracks = ((List<Map<String, Object>>) response.getBody().get("items"));
            Collections.shuffle(top50Tracks);
    
            //Make sure  recognitionQuizAnswers and recognitionQuizOptions are empty before adding answers and options to them
            recognitionQuizAnswers.clear();
            recognitionQuizOptions.clear();

            for (int i = 0; i < 5; i++) {
                Map<String, Object> track  = (((List<Map<String, Object>>) response.getBody().get("items"))).get(i);
                recognitionQuizAnswers.add(track);
              }

              for (int i = 5; i < 20; i++) {
                Map<String, Object> track  = (((List<Map<String, Object>>) response.getBody().get("items"))).get(i);
                recognitionQuizOptions.add(track);
              }

            // session.setAttribute("top5Tracks", recognitionQuizAnswers);
    
            // return recognitionQuizAnswers;
          
    }


}
