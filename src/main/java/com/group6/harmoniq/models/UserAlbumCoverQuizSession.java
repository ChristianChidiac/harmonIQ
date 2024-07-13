package com.group6.harmoniq.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;

@Entity
@Table(name = "user_album_cover_quiz_sessions")
public class UserAlbumCoverQuizSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private AlbumCoverQuiz quiz;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(name = "started_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime started_at;
    @Column(name = "completed_at")
    private LocalDateTime finished_at;

    @OneToMany(mappedBy = "userQuizSession", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserAlbumCoverQuizAnswers> answers = new ArrayList<>();

    public UserAlbumCoverQuizSession(AlbumCoverQuiz quiz, User user) {
        this.quiz = quiz;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public AlbumCoverQuiz getQuiz() {
        return quiz;
    }

    public void setQuiz(AlbumCoverQuiz quiz) {
        this.quiz = quiz;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getStarted_at() {
        return started_at;
    }

    public LocalDateTime getFinished_at() {
        return finished_at;
    }

    public void setFinished_at(LocalDateTime finished_at) {
        this.finished_at = finished_at;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setStarted_at(LocalDateTime started_at) {
        this.started_at = started_at;
    }
}
