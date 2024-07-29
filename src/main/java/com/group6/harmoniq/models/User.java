package com.group6.harmoniq.models;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;

@Entity
@Table(name = "users")
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long uid;

    // Enforce SpotifyID uniqueness and ensures every user has Spotify user ID
    @Column(unique = true, nullable = false)
    private String spotifyId;
    
    private String displayName;

    private String email;

    private int followers;

    private String imageUrl;

    private String externalSpotifyUrl;

    @Column(columnDefinition = "int default 0")
    private int addedSongs = 0;

    @Column(columnDefinition = "int default 5")
    private int addedSongsLimit = 5;

    @Transient
    private Artist topArtist;

    @Transient
    private Track topTrack;

    @Column(columnDefinition = "int default 0")
    private int quizCount = 0;

    @Column(columnDefinition = "int default 0")
    private int totalQuestionsAnswered = 0;

    @Column(columnDefinition = "int default 0")
    private int totalCorrectAnswers = 0;

    @Column(columnDefinition = "double precision default 0.0")
    private double quizScoreAverage = 0.0;
    
    @Column(columnDefinition = "boolean default false")
    private Boolean isAdmin = false;

    @Column(columnDefinition = "boolean default false")
    private Boolean isCollaborator = false;
    
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();

    @Column(updatable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt = new Date();

    // Constructors
    public User() {}

    public User(String spotifyId, String displayName, String email, int followers,  String imageUrl, String externalSpotifyUrl, int addedSongs, int addedSongsLimit, Artist topArtist, Track topTrack, Boolean isAdmin, Boolean isCollaborator) {
        this.spotifyId = spotifyId;
        this.displayName = displayName;
        this.email = email;
        this.followers = followers;
        this.imageUrl = imageUrl;
        this.externalSpotifyUrl = externalSpotifyUrl;
        this.addedSongs = addedSongs;
        this.addedSongsLimit = addedSongsLimit;
        this.topArtist = topArtist;
        this.topTrack = topTrack;
        this.isAdmin = isAdmin != null ? isAdmin : false; // Ensures admin is not null but still defaults to false
        this.isCollaborator = isCollaborator != null ? isCollaborator : false; // Ensures collaborator is not null but still defaults to false
    }

    public int getAddedSongs() {
        return addedSongs;
    }

    public void setAddedSongs(int addedSongs) {
        this.addedSongs = addedSongs;
    }

    public int getAddedSongsLimit() {
        return addedSongsLimit;
    }

    public void setAddedSongsLimit(int addedSongsLimit) {
        this.addedSongsLimit = addedSongsLimit;
    }

    public Boolean getIsCollaborator() {
        return isCollaborator;
    }

    public void setIsCollaborator(Boolean isCollaborator) {
        this.isCollaborator = isCollaborator;
    }

    public long getUId() {
        return uid;
    }

    public String getSpotifyId() {
        return spotifyId;
    }

    public void setSpotifyId(String spotifyId) {
        this.spotifyId = spotifyId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getExternalSpotifyUrl() {
        return externalSpotifyUrl;
    }

    public void setExternalSpotifyUrl(String externalSpotifyUrl) {
        this.externalSpotifyUrl = externalSpotifyUrl;
    }

    public Artist getTopArtist() {
        return topArtist;
    }

    public void setTopArtist(Artist topArtist) {
        this.topArtist = topArtist;
    }

    public Track getTopTrack() {
        return topTrack;
    }

    public void setTopTrack(Track topTrack) {
        this.topTrack = topTrack;
    }

    public int getQuizCount() {
        return quizCount;
    }

    public void incrementQuizCount() {
        this.quizCount++;
    }

    public int getTotalCorrectAnswers() {
        return totalCorrectAnswers;
    }

    public int getTotalQuestionsAnswered() {
        return totalQuestionsAnswered;
    }

    public double getQuizScoreAverage() {
        return quizScoreAverage;
    }

    public void setTotalCorrectAnswers(int totalCorrectAnswers) {
        this.totalCorrectAnswers = totalCorrectAnswers;
    }

    public void setTotalQuestions(int totalQuestionsAnswered) {
        this.totalQuestionsAnswered = totalQuestionsAnswered;
    }

    public void setQuizScoreAverage(double quizScoreAverage) {
        this.quizScoreAverage = quizScoreAverage;
    }


    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }


    
}
