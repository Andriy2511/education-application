package com.example.educationplatform.DTO;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EnglishLevelWrapper {
    List<EnglishLevelDTO> englishLevels;
}
