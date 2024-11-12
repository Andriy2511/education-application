package com.example.educationplatform.DTO;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class QuestionDTO {
    private Long testId;

    @NotNull(message = "Це поле не може бути пустим")
    @NotBlank(message = "Це поле не може бути пустим")
    private String text;

    private List<String> options;

    @Min(value = 1, message = "Значення поля повинно бути від 1 до 4")
    @Max(value = 4, message = "Значення поля повинно бути від 1 до 4")
    private Integer correctOptionIndex;
}
