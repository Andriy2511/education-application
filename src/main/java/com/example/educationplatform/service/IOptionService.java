package com.example.educationplatform.service;

import com.example.educationplatform.model.Option;
import com.example.educationplatform.model.Question;

import java.util.List;

public interface IOptionService {
    Option addOption(Option option);

    Option getOptionByText(String text);

    void deleteOption(Option option);

    List<Option> getOptionsByQuestion(Question question);
}
