package com.example.educationplatform.repository;

import com.example.educationplatform.model.EnglishLevel;
import com.example.educationplatform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface EnglishLevelRepository extends JpaRepository<EnglishLevel, Long> {
    boolean existsByName(String name);
    EnglishLevel findByName(String name);
    List<EnglishLevel> findEnglishLevelByUsersIn(Collection<User> users);
}
