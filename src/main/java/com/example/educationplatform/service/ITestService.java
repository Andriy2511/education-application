package com.example.educationplatform.service;

import com.example.educationplatform.model.Test;

import java.util.List;

public interface ITestService {
    Test addTest(Test test);

    boolean isTestExistCheckByName(String name);

    Test getTestByName(String name);

    Test getTestById(Long id);

    void deleteTest(Test test);

    List<Test> findAllTestByCompleted(boolean isCompleted, int page, int pageSize);
}
