package com.example.educationplatform.model;

import javax.persistence.*;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tests")
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "this field cannot be void")
    @NotBlank(message = "this field cannot be void")
    private String name;
    @Column(length = 10000)
    private String description;
    private boolean completedStatus;

    @OneToMany
    @ToString.Exclude
    private List<Exam> exams;

    @OneToMany
    @ToString.Exclude
    List<Question> questions;


}
