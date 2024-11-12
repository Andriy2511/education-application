package com.example.educationplatform.repository;

import com.example.educationplatform.model.Lesson;
import com.example.educationplatform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> getLessonByStudent(User student);
}
