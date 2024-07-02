package com.group6.harmoniq.services;

import org.springframework.stereotype.Service;

import com.group6.harmoniq.models.Artist;

@Service
public class SpotifyService {
    public SpotifyService() {
    }

    public Artist getTopArtist() {
        var artist = new Artist();
        artist.setName("Harry Styles");
        artist.setSpotifyUrl("https://open.spotify.com/artist/06HL4z0CvFAxyc27GXpf02");
        artist.setImageUrl("https://i.scdn.co/image/ab67616100005174e672b5f553298dcdccb0e676");
        return artist;
    }
}
