package com.group6.harmoniq.models;

import jakarta.persistence.*;

@Entity
@Table(name = "recognitionquiz")
public class RecognitionQuiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String preview_url;
    private String answer;
    private String option1;
    private String option2;
    private String option3;

    public RecognitionQuiz() {
    }
    
    public RecognitionQuiz(String preview_url, String answer, String option1, String option2, String option3) {
        this.preview_url = preview_url;
        this.answer = answer;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
    }
    public Long getId() {
        return id;
    }
    
    public String getPreviewUrl() {
        return preview_url;
    }
    public void setPreviewUrl(String preview_url) {
        this.preview_url = preview_url;
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
