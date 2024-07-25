package com.group6.harmoniq.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class UserTest {

    private User user;
    private static final String spotifyId = "31rryrxu5pswlcjpb2qlrhrhqyyq";
    private static final String displayName = "harmonIQ-Test-User";
    private static final String email = "harmoniq.test.user@gmail.com";
    private static final int followers = 0;
    private static final String imageUrl = "https://i.scdn.co/image/ab6775700000ee85637ec8caf0f0e2542dfddf9a";
    private static final String externalSpotifyUrl = "https://open.spotify.com/user/31rryrxu5pswlcjpb2qlrhrhqyyq";
    private static final int addedSongs = 0;
    private static final int addedSongsLimit = 5;
    private static final Artist topArtist = new Artist();
    private static final Track topTrack = new Track();
    private static final Boolean isAdmin = false;
    private static final Boolean isCollaborator = false;


    @BeforeEach
    public void setUp() {
        user = new User(spotifyId, displayName, email, followers, imageUrl, externalSpotifyUrl, addedSongs, addedSongsLimit, topArtist, topTrack, isAdmin, isCollaborator);
    }

    @Test
    void testGetCreatedAt() {
        assertNotNull(user.getCreatedAt());
    }

    @Test
    void testGetDisplayName() {
        assertEquals(displayName, user.getDisplayName());
    }

    @Test
    void testGetEmail() {
        assertEquals(email, user.getEmail());
    }

    @Test
    void testGetExternalSpotifyUrl() {
        assertEquals(externalSpotifyUrl, user.getExternalSpotifyUrl());
    }

    @Test
    void testGetFollowers() {
        assertEquals(followers, user.getFollowers());
    }

    @Test
    void testGetImageUrl() {
        assertEquals(imageUrl, user.getImageUrl());
    }

    @Test
    void testGetIsAdmin() {
        assertEquals(isAdmin, user.getIsAdmin());
    }

    @Test
    void testGetSpotifyId() {
        assertEquals(spotifyId, user.getSpotifyId());
    }

    @Test
    void testGetTopArtist() {
        assertEquals(topArtist, user.getTopArtist());
    }

    @Test
    void testGetTopTrack() {
        assertEquals(topTrack, user.getTopTrack());
    }

    @Test
    void testGetUpdatedAt() {
        assertNotNull(user.getUpdatedAt());
    }

    @Test
    void testSetDisplayName() {
        String newDisplayName = "HarmonIQ-Admin";
        user.setDisplayName(newDisplayName);
        assertEquals(newDisplayName, user.getDisplayName());
    }

    @Test
    void testSetEmail() {
        String newEmail = "HarmonIQ.Admin@gmail.com";
        user.setEmail(newEmail);
        assertEquals(newEmail, user.getEmail());
    }

    @Test
    void testSetExternalSpotifyUrl() {
        String newExternalSpotifyUrl = "https://open.spotify.com/user/31d75i2qdqc7d2ty6hcu3brhsgu4";
        user.setExternalSpotifyUrl(newExternalSpotifyUrl);
        assertEquals(newExternalSpotifyUrl, user.getExternalSpotifyUrl());
    }

    @Test
    void testSetFollowers() {
        int newFollowers = 4;
        user.setFollowers(newFollowers);
        assertEquals(newFollowers, user.getFollowers());
    }

    @Test
    void testSetImageUrl() {
        String newImageUrl = "https://i.scdn.co/image/ab6775700000ee85f2a0451f6461a49c870e6e00";
        user.setImageUrl(newImageUrl);
        assertEquals(newImageUrl, user.getImageUrl());
    }

    @Test
    void testSetIsAdmin() {
        Boolean newIsAdmin = true;
        user.setIsAdmin(newIsAdmin);
        assertEquals(newIsAdmin, user.getIsAdmin());
    }

    @Test
    void testSetSpotifyId() {
        String newSpotifyId = "31d75i2qdqc7d2ty6hcu3brhsgu4";
        user.setSpotifyId(newSpotifyId);
        assertEquals(newSpotifyId, user.getSpotifyId());
    }

    @Test
    void testSetTopArtist() {
        Artist newTopArtist = new Artist(1, "06HL4z0CvFAxyc27GXpf02", "Taylor Swift", "https://open.spotify.com/artist/06HL4z0CvFAxyc27GXpf02", "https://i.scdn.co/image/ab6761610000e5ebe672b5f553298dcdccb0e676");
        user.setTopArtist(newTopArtist);
        assertEquals(newTopArtist, user.getTopArtist());
    }

    @Test
    void testSetTopTrack() {
        Artist topTrackArtist = new Artist(1, "06HL4z0CvFAxyc27GXpf02", "Taylor Swift", "https://open.spotify.com/artist/06HL4z0CvFAxyc27GXpf02", "https://i.scdn.co/image/ab6761610000e5ebe672b5f553298dcdccb0e676");
        List <Artist> Artists = new ArrayList<>();
        Artists.add(topTrackArtist);
        Track newTopTrack = new Track(1, "1BxfuPKGuaTgP7aM0Bbdwr", "Cruel Summer", "1BxfuPKGuaTgP7aM0Bbdwr", "https://i.scdn.co/image/ab67616d0000b273e787cffec20aa2a396a61647", Artists);
        user.setTopTrack(newTopTrack);
        assertEquals(newTopTrack, user.getTopTrack());
    }

    @Test
    void testSetUpdatedAt() {
        Date newDate = new Date();
        user.setUpdatedAt(newDate);
        assertEquals(newDate, user.getUpdatedAt());
    }
}
