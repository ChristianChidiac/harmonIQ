package com.group6.harmoniq.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.group6.harmoniq.models.AlbumCoverQuiz;
import com.group6.harmoniq.models.AlbumCoverQuizQuestion;
import com.group6.harmoniq.models.Track;
import com.group6.harmoniq.repositories.AlbumCoverQuizQuestionRepository;
import com.group6.harmoniq.repositories.AlbumCoverQuizRepository;
import org.springframework.ai.chat.client.ChatClient;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.ai.chat.client.ChatClient;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class AlbumCoverQuizController {
    @Autowired
    private final AlbumCoverQuizRepository albumCoverQuizRepo;
    private final AlbumCoverQuizQuestionRepository AlbumCoverQuizQuestionRepo;
    private final ChatClient chatClient;

    private int currentQuestionIndex = 0;
    private int score = 0;

    // Constructor for the AlbumCoverQuizController
    public AlbumCoverQuizController(ChatClient chatClient, AlbumCoverQuizRepository albumCoverQuizRepo, AlbumCoverQuizQuestionRepository AlbumCoverQuizQuestionRepo) {
        this.albumCoverQuizRepo = albumCoverQuizRepo;
        this.chatClient = chatClient;
        this.AlbumCoverQuizQuestionRepo = AlbumCoverQuizQuestionRepo;
    }

    // Get all quizzes to display on the page

    @GetMapping("/album-cover-quiz/getAllQuizzes")
    public String getMethodName(Model model, HttpServletRequest request, HttpSession session) {
        List<AlbumCoverQuiz> albumCoverQuizzes = albumCoverQuizRepo.findAll();
        model.addAttribute("albumCoverQuizzes", albumCoverQuizzes);
        for (AlbumCoverQuiz albumCoverQuiz : albumCoverQuizzes) {
            System.out.println(albumCoverQuiz.getQuestions());
        }
        return "quizzes/AlbumCoverQuizzes";
    }

    //Generating a new quiz, recieved data using session TODO: need to delete static quiz.html
    @GetMapping("/album-cover-quiz/ai/quiz/generate")
    public String generateQuestion(HttpSession session, Model model) {
        List<Track> allTracks = (List<Track>) session.getAttribute("tracks");
        if (allTracks == null) {
            // Handle the case where there are no tracks in the session
            return "quizzes/errorPage"; // Or some other appropriate action
        }
        List<Track> quizTracks = getQuizTracks(allTracks, 5);
        completion(quizTracks);

        return "redirect:/album-cover-quiz/getAllQuizzes";
    }

    public static List<Track> getQuizTracks(List<Track> allTracks, int count) {
        List<Track> tempTracks = new ArrayList<>(allTracks);
        Collections.shuffle(tempTracks);
        return tempTracks.subList(0, count);
    }


public void completion(List<Track> tracks) {
    // Receive the tracks from the user
	AlbumCoverQuiz resultQuiz = new AlbumCoverQuiz();
    for (Track track : tracks) {
    String albumName = track.getAlbumName();
    String albumCoverUrl = track.getAlbumCoverUrl();

    // Prompt for openai chat
    String message = """
        I will give album's name, your job is to give 3 other albums of other artists that are similar to the album I gave you in JSON format. I do not need the artist's name, just the album's name. Put initial album name in answer field.: 
        ["...", "...", "..."]
        For example, if I give you "Thriller" by Michael Jackson, you can give me ["Bad", "Dangerous", "Off the Wall"]
        """;
    
    String rawResponse = chatClient.prompt()
                                 .user(message + albumName)
                                 .call()
                                 .content();
    System.out.println(rawResponse);  // Print the raw response from the chatbot (for debugging)
    ObjectMapper objectMapper = new ObjectMapper();
    try {
        List<String> options = objectMapper.readValue(rawResponse, new TypeReference<>() {});
		String option1 = options.get(0);
        String option2 = options.get(1);
        String option3 = options.get(2);
		System.out.println(options);
        AlbumCoverQuizQuestion quizQuestion = new AlbumCoverQuizQuestion(albumCoverUrl, albumName, option1, option2, option3);
        quizQuestion.setAlbumCoverQuiz(resultQuiz);
        resultQuiz.getQuestions().add(quizQuestion);
        } catch (JsonProcessingException | IndexOutOfBoundsException e) {
        // Handle JSON parsing errors
        System.out.println("Error parsing JSON");
        return;
        }
    }
    resultQuiz.setTitle("Album Cover Quiz"); // TODO: Currently every quiz has the same title and description
    resultQuiz.setDescription("Guess the album cover based on the album name"); 
    albumCoverQuizRepo.save(resultQuiz);
    System.out.println("Quiz saved");
    return;
    }

    @GetMapping("/album-cover-quiz/quiz/{quizId}")
    public String startQuiz(@PathVariable Long quizId, HttpSession session, Model model) {
            // Retrieve the quiz by quizId
        AlbumCoverQuiz quiz = albumCoverQuizRepo.findById(quizId);
        if (quiz == null) {
            return "quizzes/errorPage";  // redirect to an error page 
        }
        // Store quiz details in session for subsequent question displays
        session.setAttribute("quiz", quiz); 
        session.setAttribute("score", 0); 
        // Retrieve the first question after shuffling (if needed)
        List<AlbumCoverQuizQuestion> allQuestions = quiz.getQuestions();
        Long firstQuestionId = allQuestions.get(0).getId();
        session.setAttribute("questionId", firstQuestionId);

        return "redirect:/album-cover-quiz/quiz/" + quizId + "/question/" + firstQuestionId;
    }

    @GetMapping("/album-cover-quiz/quiz/{quizId}/question/{questionId}")
    public String getQuiz(@PathVariable Long questionId, Long quizId, HttpSession session, Model model) {
        Long currentQuestionId = questionId;
        AlbumCoverQuizQuestion question = AlbumCoverQuizQuestionRepo.findById(currentQuestionId);
        if (currentQuestionId == null || !currentQuestionId.equals(questionId)) {
            return "quizzes/errorPage";  // redirect to an error page 
        }
        if (currentQuestionIndex >= 5) {
            // Redirect to the result page
            return "redirect:/album-cover-quiz/quiz/" + quizId + "/result";
        } else {
            List<String> options = Arrays.asList(question.getAnswer(), question.getOption1(), question.getOption2(), question.getOption3());
            Collections.shuffle(options);

            model.addAttribute("question", question);
            model.addAttribute("options", options);
            model.addAttribute("questionId", question.getId());
            model.addAttribute("quizId", quizId);
        }
            
            return "quizzes/AlbumCoverQuizQuestion"; // Redirect to the quiz page
    }
    

    @PostMapping("/album-cover-quiz/quiz/{quizId}/question/{questionId}/submit")
    public String processAnswer(@RequestParam("selectedOption") String selectedOption, @RequestParam("questionId") Long questionId, Model model, HttpSession session) {
        AlbumCoverQuiz quiz = AlbumCoverQuizQuestionRepo.findById(questionId).getAlbumCoverQuiz();
        List<AlbumCoverQuizQuestion> allQuestion = AlbumCoverQuizQuestionRepo.findByAlbumCoverQuiz(quiz);
    
        // check the size of allQuestion here
        if (allQuestion.size() < 5) {
            // Handle the case where there are less than 5 questions. 
            return "quizzes/errorPage"; 
        }
    
        AlbumCoverQuizQuestion question = allQuestion.get(currentQuestionIndex);
        if (question != null && selectedOption.equals(question.getAnswer())) {
            score++; // Increment score for correct answer
            model.addAttribute("result", "Correct!"); //TODO: add score design and logic
        } else {
            model.addAttribute("result", "Incorrect.");
        }
        currentQuestionIndex++; // Move to the next question
    
        // Redirect to the next quiz question or result page
        if (currentQuestionIndex < allQuestion.size()) { // Use allQuestion.size()
            return "redirect:/album-cover-quiz/quiz/" + quiz.getId() + "/question/" + allQuestion.get(currentQuestionIndex).getId();
        } else {
            model.addAttribute("score", score);
            return "quizzes/quizResult";
        }
    }


}
