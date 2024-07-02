package com.group6.harmoniq.models;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

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


    // Default is regular user
    private Boolean isAdmin = false;
    
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private final Date createdAt = new Date();

    @Column(updatable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt = new Date();

    

    // Constructors
    public User() {}

    public User(long uid, String spotifyId, String displayName, String email, int followers,  String imageUrl, Boolean isAdmin) {
        this.uid = uid;
        this.spotifyId = spotifyId;
        this.displayName = displayName;
        this.email = email;
        this.followers = followers;
        this.imageUrl = imageUrl;
        this.isAdmin = isAdmin != null ? isAdmin : false; // Ensures admin is not null but still defaults to false
    }

    public long getUId() {
        return uid;
    }

    public void setUId(long id) {
        this.uid = id;
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
