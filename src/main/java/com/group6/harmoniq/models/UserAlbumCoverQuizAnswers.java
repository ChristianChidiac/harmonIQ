package com.group6.harmoniq.models;

import jakarta.persistence.*;

@Entity
@Table(name = "quiz_answers")
public class UserAlbumCoverQuizAnswers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_quiz_session_id", nullable = false)
    private UserAlbumCoverQuizSession user_quiz_session;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private AlbumCoverQuizQuestion question;
    @Column(name = "user_answer", nullable = false)
    String user_answer;


    public UserAlbumCoverQuizAnswers(UserAlbumCoverQuizSession user_quiz_session, AlbumCoverQuizQuestion question, String user_answer) {
        this.user_quiz_session = user_quiz_session;
        this.question = question;
        this.user_answer = user_answer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserAlbumCoverQuizSession getUser_quiz_session() {
        return user_quiz_session;
    }

    public void setUser_quiz_session(UserAlbumCoverQuizSession user_quiz_session) {
        this.user_quiz_session = user_quiz_session;
    }

    public AlbumCoverQuizQuestion getQuestion() {
        return question;
    }

    public void setQuestion(AlbumCoverQuizQuestion question) {
        this.question = question;
    }

    public String getUser_answer() {
        return user_answer;
    }

    public void setUser_answer(String user_answer) {
        this.user_answer = user_answer;
    }
}
