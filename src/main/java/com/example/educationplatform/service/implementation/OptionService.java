package com.example.educationplatform.service.implementation;

import com.example.educationplatform.service.IOptionService;
import com.example.educationplatform.model.Option;
import com.example.educationplatform.model.Question;
import com.example.educationplatform.repository.OptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OptionService implements IOptionService {

    private final OptionRepository optionRepository;

    @Autowired
    public OptionService(OptionRepository optionRepository) {
        this.optionRepository = optionRepository;
    }

    @Override
    public Option addOption(Option option){
        return optionRepository.save(option);
    }

    @Override
    public Option getOptionByText(String text){
        return optionRepository.getOptionByText(text);
    }

    @Override
    public void deleteOption(Option option){
        optionRepository.delete(option);
    }

    @Override
    public List<Option> getOptionsByQuestion(Question question){
        return optionRepository.getOptionByQuestion(question);
    }
}
