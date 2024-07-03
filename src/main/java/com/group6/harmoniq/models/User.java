package com.group6.harmoniq.models;

public class User {
    private String displayName;
    private String email;
    private String externalSpotifyUrl;
    private int followers;
    private String id;
    private String imageUrl;
    private Artist topArtist;
    private Track topTrack;

    public User() {
    }

    public User(String displayName, String email, String externalSpotifyUrl, int followers, String id,
            String imageUrl, Artist topArtist, Track topTrack) {
        this.displayName = displayName;
        this.email = email;
        this.externalSpotifyUrl = externalSpotifyUrl;
        this.followers = followers;
        this.id = id;
        this.imageUrl = imageUrl;
        this.topArtist = topArtist;
        this.topTrack = topTrack;
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

    public String getExternalSpotifyUrl() {
        return externalSpotifyUrl;
    }

    public void setExternalSpotifyUrl(String externalSpotifyUrl) {
        this.externalSpotifyUrl = externalSpotifyUrl;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

}
