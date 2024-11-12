package com.example.educationplatform.repository;

import com.example.educationplatform.model.Exam;
import com.example.educationplatform.model.Test;
import com.example.educationplatform.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
    Exam getExamById(Long id);
    List<Exam> getExamByStudent(User user, Pageable pageable);
    List<Exam> getExamByTest(Test test, Pageable pageable);
}
