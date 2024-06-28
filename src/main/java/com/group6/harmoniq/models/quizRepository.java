package com.group6.harmoniq.models;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface quizRepository extends JpaRepository<quiz,Long>{
    List<quiz> findByQuestion(String question);
}
