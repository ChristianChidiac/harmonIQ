package com.group6.harmoniq.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.group6.harmoniq.models.AlbumCoverQuiz;
import java.util.List;


public interface AlbumCoverQuizRepository extends JpaRepository<AlbumCoverQuiz, Integer> {
    AlbumCoverQuiz findById(Long Id);
}
