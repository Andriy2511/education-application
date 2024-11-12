package com.example.educationplatform.DTO;

import com.example.educationplatform.model.Option;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AnswerDTO {
    String textQuestion;
    String correctAnswer;
    String chosenAnswer;
    List<Option> options;
}
