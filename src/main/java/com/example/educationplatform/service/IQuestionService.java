package com.example.educationplatform.service;

import com.example.educationplatform.model.Question;
import com.example.educationplatform.model.Test;

import java.util.List;

public interface IQuestionService {
    Question addQuestion(Question question);

    Question findQuestionById(Long id);

    List<Question> getQuestionByTest(Test test);

    void deleteQuestion(Question question);
}
