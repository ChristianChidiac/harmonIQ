package com.group6.harmoniq.models;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findBySpotifyId(String spotifyId);
    User findByUid(Long uid);

    void deleteBySpotifyId(String spotifyId);
    void deleteByUid(long uid);

  
    List<User> findAll();
}
