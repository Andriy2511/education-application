package com.example.educationplatform.service;

import com.example.educationplatform.model.EnglishLevel;
import com.example.educationplatform.model.User;

import java.util.List;

public interface IEnglishLevelService {
    EnglishLevel addEnglishLevel(EnglishLevel englishLevel);

    boolean isExistByName(String name);

    EnglishLevel findByName(String name);

    List<EnglishLevel> getAllEnglishLevels();

    List<EnglishLevel> findEnglishLevelsByUser(User user);
}
