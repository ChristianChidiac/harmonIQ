package com.group6.harmoniq.models;

import jakarta.persistence.*;

@Entity
@Table(name = "album_cover_quiz_questions")
public class AlbumCoverQuizQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private AlbumCoverQuiz albumCoverQuiz;

    @Column(name = "question_url", nullable = false)
    private String albumCoverUrl;
    @Column(nullable = false)
    private String answer;
    @Column(nullable = false)
    private String option1;
    @Column(nullable = false)
    private String option2;
    @Column(nullable = false)
    private String option3;


    public AlbumCoverQuizQuestion(String albumCoverUrl, String answer, String option1, String option2, String option3) {
        this.albumCoverUrl = albumCoverUrl;
        this.answer = answer;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AlbumCoverQuiz getAlbumCoverQuiz() {
        return albumCoverQuiz;
    }

    public void setAlbumCoverQuiz(AlbumCoverQuiz albumCoverQuiz) {
        this.albumCoverQuiz = albumCoverQuiz;
    }

    public String getAlbumCoverUrl() {
        return albumCoverUrl;
    }

    public void setAlbumCoverUrl(String albumCoverUrl) {
        this.albumCoverUrl = albumCoverUrl;
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
