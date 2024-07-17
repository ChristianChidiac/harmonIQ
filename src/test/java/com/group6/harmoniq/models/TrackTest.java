package com.group6.harmoniq.models;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.util.Assert;

public class TrackTest {

    private Track track;
    private static final long uId = 1L;
    private static final String spotifyId = "7tFiyTwD0nx5a1eklYtX2J";
    private static final String name = "Bohemian Rhapsody";
    private static final String spotifyUrl = "https://open.spotify.com/track/7tFiyTwD0nx5a1eklYtX2J";
    private static final String albumCoverUrl = "https://i.scdn.co/image/ab67616d0000b273ce4f1737bc8a646c8c4bd25a";
    private static final Artist artist1 = new Artist(1L, "1dfeR4HaWDbWqFHLkxsg1d", "Queen", "https://open.spotify.com/artist/1dfeR4HaWDbWqFHLkxsg1d", "https://i.scdn.co/image/b040846ceba13c3e9c125d68389491094e7f2982");
    private static final List<Artist> artists = new ArrayList<>();
    static{
        artists.add(artist1);
    }

    @BeforeEach
    public void setUp() {
        track = new Track(uId, spotifyId, name, spotifyUrl, albumCoverUrl, artists);
    }

    @Test
    void testGetuId() {
        assertEquals(uId, track.getuId());
    }

    @Test
    void testSetuId() {
        long newSetuId = 2L;
        track.setuId(newSetuId);
        assertEquals(newSetuId, track.getuId());
    }

    @Test
    void testGetAlbumCoverUrl() {
        assertEquals(albumCoverUrl, track.getAlbumCoverUrl());
    }

    @Test
    void testGetArtists() {
        assertEquals(artists, track.getArtists());
    }

    @Test
    void testGetName() {
        assertEquals(name, track.getName());
    }

    @Test
    void testGetSpotifyId() {
        assertEquals(spotifyId, track.getSpotifyId());
    }

    @Test
    void testGetSpotifyUrl() {
        assertEquals(spotifyUrl, track.getSpotifyUrl());
    }

    @Test
    void testSetAlbumCoverUrl() {
        String newAlbumCoverUrl = "https://i.scdn.co/image/ab67616d0000b273008b06ec71019afd70153889";
        track.setAlbumCoverUrl(newAlbumCoverUrl);
        assertEquals(newAlbumCoverUrl, track.getAlbumCoverUrl());
    }

    @Test
    void testSetArtists() {
        Artist artist2 = new Artist(2L, "3WrFJ7ztbogyGnTHbHJFl2", "The Beatles", "https://open.spotify.com/artist/3WrFJ7ztbogyGnTHbHJFl2", "https://i.scdn.co/image/ab6761610000e5ebe9348cc01ff5d55971b22433");
        artists.clear();
        artists.add(artist2);
        track.setArtists(artists);
        assertEquals(artists, track.getArtists());
    }

    @Test
    void testSetName() {
        String newName = "Yesterday";
        track.setName(newName);
        assertEquals(newName, track.getName());
    }

    @Test
    void testSetSpotifyId() {
        String newSpotifyId = "3BQHpFgAp4l80e1XslIjNI";
        track.setSpotifyId(newSpotifyId);
        assertEquals(newSpotifyId, track.getSpotifyId());
    }

    @Test
    void testSetSpotifyUrl() {
        String newSpotifyUrl = "https://open.spotify.com/track/3BQHpFgAp4l80e1XslIjNI";
        track.setSpotifyUrl(newSpotifyUrl);
        assertEquals(newSpotifyUrl, track.getSpotifyUrl());
    }
}
