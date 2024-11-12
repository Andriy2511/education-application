package com.example.educationplatform.service.implementation;

import com.example.educationplatform.service.IExamService;
import com.example.educationplatform.model.Exam;
import com.example.educationplatform.model.Test;
import com.example.educationplatform.model.User;
import com.example.educationplatform.repository.ExamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamService implements IExamService {
    private final ExamRepository examRepository;

    @Autowired
    public ExamService(ExamRepository examRepository) {
        this.examRepository = examRepository;
    }

    @Override
    public Exam addExam(Exam exam){
        return examRepository.save(exam);
    }

    @Override
    public Exam getExamById(Long id){
        return examRepository.getExamById(id);
    }

    @Override
    public List<Exam> getExamsByUser(User user, int page, int pageSize){
        Pageable pageable = PageRequest.of(page, pageSize);
        return examRepository.getExamByStudent(user, pageable);
    }

    @Override
    public List<Exam> getExamsByTest(Test test, int page, int pageSize){
        Pageable pageable = PageRequest.of(page, pageSize);
        return examRepository.getExamByTest(test, pageable);
    }
}
