package com.aidemo.aidemo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.aidemo.aidemo.models.AlbumCoverQuiz;
import com.aidemo.aidemo.repositories.AlbumCoverQuizRepository;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class AlbumCoverQuizController {
    @Autowired
    private AlbumCoverQuizRepository albumCoverQuizRepo;

    private List<AlbumCoverQuiz> albumCoverQuizzes;
    private int currentQuizIndex = 0;
    private int score = 0;

    

}
