package com.group6.harmoniq.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.group6.harmoniq.models.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findBySpotifyId(String spotifyId);
    User findByUid(Long uid);

    void deleteBySpotifyId(String spotifyId);
    void deleteByUid(long uid);
}
