package com.example.educationplatform.service;

import com.example.educationplatform.model.Lesson;
import com.example.educationplatform.model.User;

import java.util.List;

public interface ILessonService {
    Lesson addNewLesson(Lesson lesson);

    List<Lesson> getLessonsByStudent(User student);
}
