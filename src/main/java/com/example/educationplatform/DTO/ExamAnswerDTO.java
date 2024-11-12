package com.example.educationplatform.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ExamAnswerDTO {
    private Long questionId;
    private Integer answerIndex;
}
