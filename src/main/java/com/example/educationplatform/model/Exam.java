package com.example.educationplatform.model;

import javax.persistence.*;
import lombok.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "exams")
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer maxResult;
    private Integer result;
    private Date date;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private User student;

    @ManyToOne
    @JoinColumn(name = "test_id")
    private Test test;

    @OneToMany
    @ToString.Exclude
    private List<ExamAnswer> examAnswer;
}
