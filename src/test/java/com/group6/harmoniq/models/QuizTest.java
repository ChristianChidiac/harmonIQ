package com.group6.harmoniq.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

public class QuizTest {

    private AlbumQuiz albumQuiz;
    private static final List<QuizQuestion> questions = new ArrayList<>();
    private QuizQuestion quiz;
    private static final Long id = 1L;
    private static final String question_url = "https://i.scdn.co/image/ab67616d0000b273ce4f1737bc8a646c8c4bd25a";
    private static final String answer = "Bohemian Rhapsody";
    private static final String option1 = "Post Malone";
    private static final String option2 = "Led Zeppelin";
    private static final String option3 = "The Rolling Stones";
    private static final QuizQuestion quiz1 = new QuizQuestion(1L, "https://i.scdn.co/image/ab67616d0000b273ce4f1737bc8a646c8c4bd25a", "Bohemian Rhapsody", "Post Malone", "Led Zeppelin", "The Rolling Stones");
    static{
        questions.add(quiz1);
    }

    @BeforeEach
    public void setUp() {
        quiz = new QuizQuestion(id, question_url, answer, option1, option2, option3);
        albumQuiz = new AlbumQuiz(1L, questions);
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
        assertEquals(question_url, quiz.getQuestionUrl()); 
    }

    @Test
    void testGetQuestions() {
        assertEquals(questions, albumQuiz.getQuestions());
    }

    @Test
    void testSetQuestions() {
        List<QuizQuestion> newQuestions = new ArrayList<>();
        newQuestions.add(quiz);
        albumQuiz.setQuestions(newQuestions);
        assertEquals(newQuestions, albumQuiz.getQuestions());
    }

    @Test
    void testSetAnswer() {
        String newAnswer = "The Beatles";
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
        String newOption3 = "Taylor Swift" ;
        quiz.setOption3(newOption3);
        assertEquals(newOption3, quiz.getOption3());
    }

    @Test
    void testSetQuestionPath() {
        String newQuestionUrl = "https://i.scdn.co/image/ab6761610000e5ebe9348cc01ff5d55971b22433";
        quiz.setQuestionUrl(newQuestionUrl);
        assertEquals(newQuestionUrl, quiz.getQuestionUrl());
    }
}
