package com.example.educationplatform.DTO;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ExamAnswersWrapper {
    private List <ExamAnswerDTO> examAnswers;
}
