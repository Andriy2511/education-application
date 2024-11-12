package com.example.educationplatform.service.implementation;

import com.example.educationplatform.model.EnglishLevel;
import com.example.educationplatform.model.User;
import com.example.educationplatform.repository.EnglishLevelRepository;
import com.example.educationplatform.service.IEnglishLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EnglishLevelService implements IEnglishLevelService {

    private final EnglishLevelRepository englishLevelRepository;

    @Autowired
    public EnglishLevelService(EnglishLevelRepository englishLevelRepository) {
        this.englishLevelRepository = englishLevelRepository;
    }

    @Override
    public EnglishLevel addEnglishLevel(EnglishLevel englishLevel){
        return englishLevelRepository.save(englishLevel);
    }

    @Override
    public boolean isExistByName(String name){
        return englishLevelRepository.existsByName(name);
    }

    @Override
    public EnglishLevel findByName(String name){
        return englishLevelRepository.findByName(name);
    }

    @Override
    public List<EnglishLevel> getAllEnglishLevels(){
        return englishLevelRepository.findAll();
    }

    @Override
    public List<EnglishLevel> findEnglishLevelsByUser(User user){
        List<User> users = new ArrayList<>();
        users.add(user);
        return englishLevelRepository.findEnglishLevelByUsersIn(users);
    }
}
