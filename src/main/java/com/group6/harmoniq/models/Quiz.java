package com.group6.harmoniq.models;

import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "quizzes")
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String question_url;
    private String answer;
    private List<String> options;

    public Quiz() {
    }

    public Quiz(String question_url, String answer, List<String> options) {
        this.question_url = question_url;
        this.answer = answer;
        this.options = options;
    }

    public Long getId() {
        return id;
    }

    public String getQuestion_url() {
        return question_url;
    }

    public String getAnswer() {
        return answer;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setQuestion_url(String question_url) {
        this.question_url = question_url;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }    
}
