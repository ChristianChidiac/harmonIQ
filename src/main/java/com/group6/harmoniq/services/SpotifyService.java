package com.group6.harmoniq.services;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.group6.harmoniq.models.Artist;
import com.group6.harmoniq.models.Track;

@Service
public class SpotifyService {

    private String clientId;
    private String clientSecret;
    private String redirectUri;

    public SpotifyService(
        @Value("${spotify.client.id}") String clientId, 
        @Value("${spotify.client.secret}") String clientSecret,
        @Value("${spotify.redirect.uri}") String redirectUri
    ) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public Artist getTopArtist() {
        var artist = new Artist();
        artist.setName("Harry Styles");
        artist.setSpotifyUrl("https://open.spotify.com/artist/06HL4z0CvFAxyc27GXpf02");
        artist.setImageUrl("https://i.scdn.co/image/ab67616100005174e672b5f553298dcdccb0e676");
        return artist;
    }

    public Track getTopTrack() {
        Artist artist = new Artist();
        artist.setName("Harry Styles");

        List<Artist> artists = Arrays.asList(artist);

        var track = new Track();
        track.setArtists(artists);
        track.setName("Watermelon Sugar");
        track.setSpotifyUrl("https://open.spotify.com/track/4IR5cCTkL4DjgJwtSijbjL");
        track.setAlbumCoverUrl("https://i.scdn.co/image/ab67616d00001e025aa9dbc2ad20c84e508fcd25");

        return track;
    }
}
