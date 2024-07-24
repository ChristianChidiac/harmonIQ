package com.group6.harmoniq.models;

public class QuizQuestion {
    private Long id;
    private String question_url;
    private String answer;
    private String option1;
    private String option2;
    private String option3;


    public QuizQuestion() {
    }

    public QuizQuestion(String question_url, String answer, String option1, String option2, String option3) {
        this.question_url = question_url;
        this.answer = answer;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
    }

    public Long getId() {
        return id;
    }

    public String getQuestionUrl() {
        return question_url;
    }

    public void setQuestionUrl(String question_url) {
        this.question_url = question_url;
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
