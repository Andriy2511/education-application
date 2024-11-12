package com.example.educationplatform.repository;

import com.example.educationplatform.model.Option;
import com.example.educationplatform.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {
    Option getOptionByText(String text);
    List<Option> getOptionByQuestion(Question question);
}
