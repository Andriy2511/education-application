package com.example.educationplatform.model;

import javax.persistence.*;
import javax.validation.constraints.*;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Поле рейтингу не може бути пустим")
    @Min(value = 1, message = "Значення рейтингу бути в межах від 1 до 5")
    @Max(value = 5, message = "Значення рейтингу бути в межах від 1 до 5")
    private Integer rate;

    @NotNull(message = "Поле коментаря не може бути пустим")
    @NotBlank(message = "Поле коментаря не може бути пустим")
    @Column(length = 10000)
    private String comment;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private User student;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "tutor_id")
    private User tutor;
}
