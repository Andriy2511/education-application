package com.example.educationplatform.model;

import javax.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "questions")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    @OneToOne
    private Option correctOption;

    @OneToMany
    private List<Option> options;

    @ManyToOne
    @JoinColumn(name = "test_id")
    Test test;
}
