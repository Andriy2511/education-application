package com.example.educationplatform.service.implementation;

import com.example.educationplatform.service.IExamAnswerService;
import com.example.educationplatform.model.ExamAnswer;
import com.example.educationplatform.repository.ExamAnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExamAnswerService implements IExamAnswerService {
    private final ExamAnswerRepository examAnswerRepository;

    @Autowired
    public ExamAnswerService(ExamAnswerRepository examAnswerRepository) {
        this.examAnswerRepository = examAnswerRepository;
    }

    @Override
    public ExamAnswer addAnswer(ExamAnswer examAnswer){
        return examAnswerRepository.save(examAnswer);
    }
}
