package com.example.educationplatform.service.implementation;

import com.example.educationplatform.service.IQuestionService;
import com.example.educationplatform.model.Question;
import com.example.educationplatform.model.Test;
import com.example.educationplatform.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService implements IQuestionService {

    private final QuestionRepository questionRepository;

    @Autowired
    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Override
    public Question addQuestion(Question question){
        return questionRepository.save(question);
    }

    @Override
    public Question findQuestionById(Long id){
        return questionRepository.findById(id).get();
    }

    @Override
    public List<Question> getQuestionByTest(Test test){
        return questionRepository.getQuestionsByTest(test);
    }

    @Override
    public void deleteQuestion(Question question){
        questionRepository.delete(question);
    }
}
