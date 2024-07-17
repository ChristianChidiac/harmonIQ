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

public class RecognitionQuizTest {

    private RecognitionQuiz recognitionQuiz;
    private static final long id = 1L;
    private static final String preview_url = "https://p.scdn.co/mp3-preview/fafa71e6ec5fb83cec5136e3e6a357c6233141ba?cid=cfe923b2d660439caf2b557b21f31221";
    private static final String answer = "Queen";
    private static final String option1 = "Foo Fighters";
    private static final String option2 = "The Weeknd";
    private static final String option3 = "Taylor Swift";

    @BeforeEach
    public void setUp() {
        recognitionQuiz = new RecognitionQuiz(preview_url, answer, option1, option2, option3);
        recognitionQuiz.setId(1L);
    }


    @Test
    void testGetId() {
        assertEquals(id, recognitionQuiz.getId());
    }

    @Test
    void testSetId() {
        long newSetId = 2L;
        recognitionQuiz.setId(newSetId);
        assertEquals(newSetId, recognitionQuiz.getId());
    }

    @Test
    void testGetAnswer() {
        assertEquals(answer, recognitionQuiz.getAnswer());
    }

    @Test
    void testGetOption1() {
        assertEquals(option1, recognitionQuiz.getOption1());   
    }

    @Test
    void testGetOption2() {
        assertEquals(option2, recognitionQuiz.getOption2());  
    }

    @Test
    void testGetOption3() {
        assertEquals(option3, recognitionQuiz.getOption3());  
    }

    @Test
    void testGetPreviewUrl() {
        assertEquals(preview_url, recognitionQuiz.getPreviewUrl()); 
    }

    @Test
    void testSetAnswer() {
        String newAnswer = "Taylor Swift";
        recognitionQuiz.setAnswer(newAnswer);
        assertEquals(newAnswer, recognitionQuiz.getAnswer());
    }

    @Test
    void testSetOption1() {
        String newOption1 = "The Weeknd";
        recognitionQuiz.setOption1(newOption1);
        assertEquals(newOption1, recognitionQuiz.getOption1());
    }

    @Test
    void testSetOption2() {
        String newOption2 = "The White Stripes";
        recognitionQuiz.setOption2(newOption2);
        assertEquals(newOption2, recognitionQuiz.getOption2());
    }

    @Test
    void testSetOption3() {
        String newOption3 = "The Beatles";
        recognitionQuiz.setOption3(newOption3);
        assertEquals(newOption3, recognitionQuiz.getOption3());
    }

    @Test
    void testSetPreviewUrl() {
        String newPreviewUrl = "https://p.scdn.co/mp3-preview/51c08d92815cce4ac2de94a7335a430b81234624?cid=cfe923b2d660439caf2b557b21f31221";
        recognitionQuiz.setPreviewUrl(newPreviewUrl);
        assertEquals(newPreviewUrl, recognitionQuiz.getPreviewUrl());
    }
}
