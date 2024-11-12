package com.example.educationplatform.service;

import com.example.educationplatform.model.Exam;
import com.example.educationplatform.model.Test;
import com.example.educationplatform.model.User;

import java.util.List;

public interface IExamService {
    Exam addExam(Exam exam);

    Exam getExamById(Long id);

    List<Exam> getExamsByUser(User user, int page, int pageSize);

    List<Exam> getExamsByTest(Test test, int page, int pageSize);
}
