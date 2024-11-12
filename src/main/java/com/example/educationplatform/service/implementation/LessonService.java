package com.example.educationplatform.service.implementation;

import com.example.educationplatform.model.Lesson;
import com.example.educationplatform.model.User;
import com.example.educationplatform.repository.LessonRepository;
import com.example.educationplatform.service.ILessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LessonService implements ILessonService {

    private final LessonRepository lessonRepository;

    @Autowired
    public LessonService(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }

    @Override
    public Lesson addNewLesson(Lesson lesson){
        return lessonRepository.save(lesson);
    }

    @Override
    public List<Lesson> getLessonsByStudent(User student){
        return lessonRepository.getLessonByStudent(student);
    }
}
