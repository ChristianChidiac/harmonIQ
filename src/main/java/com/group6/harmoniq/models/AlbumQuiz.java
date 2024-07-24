package com.group6.harmoniq.models;

import java.util.List;

import jakarta.persistence.*;

public class AlbumQuiz {
    private Long id;
    private List<QuizQuestion> questions;

    public AlbumQuiz() {
    }
    public AlbumQuiz(List<QuizQuestion> questions) {
        this.questions = questions;
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
