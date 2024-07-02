package com.group6.harmoniq.models;

public class Artist {
    private String uId;
    private String spotifyId;
    private String name;
    private String spotifyUrl;
    private String imageUrl;

    public Artist() {
    }

    public Artist(String uId, String spotifyId, String name, String spotifyUrl, String imageUrl) {
        this.uId = uId;
        this.spotifyId = spotifyId;
        this.name = name;
        this.spotifyUrl = spotifyUrl;
        this.imageUrl = imageUrl;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getSpotifyId() {
        return spotifyId;
    }

    public void setSpotifyId(String spotifyId) {
        this.spotifyId = spotifyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpotifyUrl() {
        return spotifyUrl;
    }

    public void setSpotifyUrl(String spotifyUrl) {
        this.spotifyUrl = spotifyUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    
}
