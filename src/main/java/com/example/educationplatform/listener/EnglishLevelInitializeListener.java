package com.example.educationplatform.listener;

import com.example.educationplatform.model.EnglishLevel;
import com.example.educationplatform.service.IEnglishLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EnglishLevelInitializeListener implements ApplicationListener<ContextRefreshedEvent> {

    private final IEnglishLevelService englishLevelService;


    @Autowired
    public EnglishLevelInitializeListener(IEnglishLevelService englishLevelService) {
        this.englishLevelService = englishLevelService;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        createEnglishLevelIfAbsent();
    }

    private void createEnglishLevelIfAbsent(){
        List<String> englishLevelsNames = getEnglishLevelsName();
        for(String englishLevelsName : englishLevelsNames){
            if(!englishLevelService.isExistByName(englishLevelsName)){
                EnglishLevel englishLevel = new EnglishLevel();
                englishLevel.setName(englishLevelsName);
                englishLevelService.addEnglishLevel(englishLevel);
            }
        }
    }

    private List<String> getEnglishLevelsName(){
        List<String> englishLevelsNames = new ArrayList<>();
        englishLevelsNames.add("A0");
        englishLevelsNames.add("A1");
        englishLevelsNames.add("A2");
        englishLevelsNames.add("B1");
        englishLevelsNames.add("B2");
        englishLevelsNames.add("C1");
        englishLevelsNames.add("C2");

        return englishLevelsNames;
    }
}
