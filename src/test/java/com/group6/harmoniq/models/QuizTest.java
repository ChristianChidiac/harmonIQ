package com.group6.harmoniq.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.util.Assert;

public class QuizTest {

    private Quiz quiz;
    private static final long id = 1L;
    private static final String question_path = "images/albumcovers/Elephant.png";
    private static final String answer = "The White Stripes";
    private static final String option1 = "Post Malone";
    private static final String option2 = "Led Zeppelin";
    private static final String option3 = "The Rolling Stones";

    @BeforeEach
    public void setUp() {
        quiz = new Quiz(question_path, answer, option1, option2, option3);
        quiz.setId(1L);
    }

    @Test
    void testGetId() {
        assertEquals(id, quiz.getId()); 
    }

    @Test
    void testSetId() {
        long newSetId = 2L;
        quiz.setId(newSetId);
        assertEquals(newSetId, quiz.getId());
    }

    @Test
    void testGetAnswer() {
        assertEquals(answer, quiz.getAnswer());
    }

    @Test
    void testGetOption1() {
        assertEquals(option1, quiz.getOption1());   
    }

    @Test
    void testGetOption2() {
        assertEquals(option2, quiz.getOption2()); 
    }

    @Test
    void testGetOption3() {
        assertEquals(option3, quiz.getOption3()); 
    }

    @Test
    void testGetQuestionPath() {
        assertEquals(question_path, quiz.getQuestionPath()); 
    }

    @Test
    void testSetAnswer() {
        String newAnswer = "Taylor Swift";
        quiz.setAnswer(newAnswer);
        assertEquals(newAnswer, quiz.getAnswer());
    }

    @Test
    void testSetOption1() {
        String newOption1 = "The Weeknd";
        quiz.setOption1(newOption1);
        assertEquals(newOption1, quiz.getOption1());
    }

    @Test
    void testSetOption2() {
        String newOption2 = "Queen";
        quiz.setOption2(newOption2);
        assertEquals(newOption2, quiz.getOption2());
    }

    @Test
    void testSetOption3() {
        String newOption3 = "The Beatles";
        quiz.setOption3(newOption3);
        assertEquals(newOption3, quiz.getOption3());
    }

    @Test
    void testSetQuestionPath() {
        String newQuestionPath = "images/albumcovers/SpeakNow.png";
        quiz.setQuestionPath(newQuestionPath);
        assertEquals(newQuestionPath, quiz.getQuestionPath());
    }
}
