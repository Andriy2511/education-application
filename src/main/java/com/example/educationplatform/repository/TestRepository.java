package com.example.educationplatform.repository;

import com.example.educationplatform.model.Test;
import com.example.educationplatform.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestRepository extends JpaRepository<Test, Long> {
    Test getTestByName(String name);
    Test getTestById(Long id);

    List<Test> findAllByCompletedStatus(boolean isCompleted, Pageable pageable);

    boolean existsTestByName(String name);
}
