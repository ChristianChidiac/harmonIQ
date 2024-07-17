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

public class ArtistTest {

    private Artist artist;
    private static final long uId = 1L;
    private static final String spotifyId = "06HL4z0CvFAxyc27GXpf02";
    private static final String name = "Taylor Swift";
    private static final String spotifyUrl = "https://open.spotify.com/artist/06HL4z0CvFAxyc27GXpf02";
    private static final String imageUrl = "https://i.scdn.co/image/ab6761610000e5ebe672b5f553298dcdccb0e676";
  
    @BeforeEach
    public void setUp() {
        artist = new Artist(uId, spotifyId, name, spotifyUrl, imageUrl);
    }

    @Test
    void testGetuId() {
        assertEquals(uId, artist.getuId());
    }

    @Test
    void testGetImageUrl() {
        assertEquals(imageUrl, artist.getImageUrl());
    }

    @Test
    void testGetName() {
        assertEquals(name, artist.getName());
    }

    @Test
    void testGetSpotifyId() {
        assertEquals(spotifyId, artist.getSpotifyId());
    }

    @Test
    void testGetSpotifyUrl() {
        assertEquals(spotifyUrl, artist.getSpotifyUrl());
    }

    @Test
    void testSetImageUrl() {
        String newImageUrl = "https://i.scdn.co/image/ab6761610000e5eb214f3cf1cbe7139c1e26ffbb";
        artist.setImageUrl(newImageUrl);
        assertEquals(newImageUrl, artist.getImageUrl());
    }

    @Test
    void testSetName() {
        String newName = "The Weeknd";
        artist.setName(newName);
        assertEquals(newName, artist.getName());
    }

    @Test
    void testSetSpotifyId() {
        String newSpotifyId = "1Xyo4u8uXC1ZmMpatF05PJ";
        artist.setSpotifyId(newSpotifyId);
        assertEquals(newSpotifyId, artist.getSpotifyId());
    }

    @Test
    void testSetSpotifyUrl() {
        String newSpotifyUrl = "https://open.spotify.com/artist/1Xyo4u8uXC1ZmMpatF05PJ";
        artist.setSpotifyUrl(newSpotifyUrl);
        assertEquals(newSpotifyUrl, artist.getSpotifyUrl());
    }

    @Test
    void testSetuId() {
        long newUid = 2L;
        artist.setuId(newUid);
        assertEquals(newUid, artist.getuId());
    }
}
