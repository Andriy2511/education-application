package com.example.educationplatform.service.implementation;

import com.example.educationplatform.model.Test;
import com.example.educationplatform.repository.TestRepository;
import com.example.educationplatform.service.ITestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestService implements ITestService {
    private final TestRepository testRepository;

    @Autowired
    public TestService(TestRepository testRepository) {
        this.testRepository = testRepository;
    }

    @Override
    public Test addTest(Test test){
        return testRepository.save(test);
    }

    @Override
    public boolean isTestExistCheckByName(String name){
        return testRepository.existsTestByName(name);
    }

    @Override
    public Test getTestByName(String name){
        return testRepository.getTestByName(name);
    }

    @Override
    public Test getTestById(Long id){
        return testRepository.getTestById(id);
    }

    @Override
    public void deleteTest(Test test){
        testRepository.delete(test);
    }

    @Override
    public List<Test> findAllTestByCompleted(boolean isCompleted, int page, int pageSize){
        Pageable pageable = PageRequest.of(page, pageSize);
        return testRepository.findAllByCompletedStatus(isCompleted, pageable);
    }
}
