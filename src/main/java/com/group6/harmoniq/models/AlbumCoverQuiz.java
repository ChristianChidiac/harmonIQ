package com.group6.harmoniq.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;

@Entity
@Table(name = "album_cover_quizzes")
public class AlbumCoverQuiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String title;
    @Column
    private String description;

    @Column(name = "craeted_at", nullable = false, updatable = false)
    @CreationTimestamp
    private final Date createdAt = new Date();

    @Column(name = "updated_at", nullable = false, updatable = true)
    @UpdateTimestamp
    private Date updatedAt = new Date();

    @OneToMany(mappedBy = "albumCoverQuiz", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<AlbumCoverQuizQuestion> questions = new ArrayList<>();

    public AlbumCoverQuiz() {}

    public AlbumCoverQuiz(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public List<AlbumCoverQuizQuestion> getQuestions() {
        return questions;
    }
}