package com.group6.harmoniq.models;

import java.util.List;

public class Track {
    private long uId;
    private String spotifyId;
    private String name;
    private String spotifyUrl;
    private String albumCoverUrl;
    private List<Artist> artists;

    public Track() {
    }

    public Track(long uId, String spotifyId, String name, String spotifyUrl, String albumCoverUrl, List<Artist> artists) {
        this.uId = uId;
        this.spotifyId = spotifyId;
        this.name = name;
        this.spotifyUrl = spotifyUrl;
        this.albumCoverUrl = albumCoverUrl;
        this.artists = artists;
    }

    public long getuId() {
        return uId;
    }

    public void setuId(long uId) {
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

    public String getAlbumCoverUrl() {
        return albumCoverUrl;
    }

    public void setAlbumCoverUrl(String albumCoverUrl) {
        this.albumCoverUrl = albumCoverUrl;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    
}
