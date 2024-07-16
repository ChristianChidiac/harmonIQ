package com.group6.harmoniq.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.group6.harmoniq.models.AlbumCoverQuiz;
import com.group6.harmoniq.models.AlbumCoverQuizQuestion;

public interface AlbumCoverQuizQuestionRepository extends JpaRepository<AlbumCoverQuizQuestion, Integer> {
    AlbumCoverQuizQuestion findById(Long Id);
    List<AlbumCoverQuizQuestion> findByAlbumCoverQuiz(AlbumCoverQuiz albumCoverQuiz);
}
