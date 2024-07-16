package com.group6.harmoniq.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.group6.harmoniq.models.UserAlbumCoverQuizSession;

public interface UserAlbumCoverQuizSessionRepository extends JpaRepository<UserAlbumCoverQuizSession, Integer> {
    UserAlbumCoverQuizSession findById(Long Id);
    
}
