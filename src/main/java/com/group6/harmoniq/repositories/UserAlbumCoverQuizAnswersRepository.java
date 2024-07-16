package com.group6.harmoniq.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.group6.harmoniq.models.UserAlbumCoverQuizAnswers;

public interface UserAlbumCoverQuizAnswersRepository extends JpaRepository<UserAlbumCoverQuizAnswers, Integer> {
    UserAlbumCoverQuizAnswers findById(Long Id);
    
}
