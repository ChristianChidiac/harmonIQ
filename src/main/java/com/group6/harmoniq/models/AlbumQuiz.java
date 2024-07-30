package com.group6.harmoniq.models;

import java.util.ArrayList;
import java.util.List;


public class AlbumQuiz {
    private Long id;
    private List<QuizQuestion> questions = new ArrayList<>();

    public AlbumQuiz() {
        this.questions = new ArrayList<>();
    }
    public AlbumQuiz(Long id, List<QuizQuestion> questions) {
        this.id = id;
        this.questions = questions;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getId() {
        return id;
    }
    public void setQuestions(List<QuizQuestion> questions) {
        this.questions = questions;
    }
    public List<QuizQuestion> getQuestions() {
        return questions;
    }
}
