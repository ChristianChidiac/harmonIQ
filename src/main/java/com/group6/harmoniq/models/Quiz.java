package com.group6.harmoniq.models;

import jakarta.persistence.*;

@Entity
@Table(name = "quizzes")
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String question_path;
    private String answer;
    private String option1;
    private String option2;
    private String option3;

    public Quiz() {
    }
    public Quiz(String question_path, String answer, String option1, String option2, String option3) {
        this.question_path = question_path;
        this.answer = answer;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
    }
    public Long getId() {
        return id;
    }
     //For testing purposes
     public void setId(Long id) {
        this.id = id;
    }
    public String getQuestionPath() {
        return question_path;
    }
    public void setQuestionPath(String question_path) {
        this.question_path = question_path;
    }
    public String getAnswer() {
        return answer;
    }
    public void setAnswer(String answer) {
        this.answer = answer;
    }
    public String getOption1() {
        return option1;
    }
    public void setOption1(String option1) {
        this.option1 = option1;
    }
    public String getOption2() {
        return option2;
    }
    public void setOption2(String option2) {
        this.option2 = option2;
    }
    public String getOption3() {
        return option3;
    }
    public void setOption3(String option3) {
        this.option3 = option3;
    }
}
