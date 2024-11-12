package com.example.educationplatform.repository;

import com.example.educationplatform.model.Question;
import com.example.educationplatform.model.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    Optional<Question> findById(Long id);
    List<Question> getQuestionsByTest(Test test);
}
