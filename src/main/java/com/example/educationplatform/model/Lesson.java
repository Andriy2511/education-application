package com.example.educationplatform.model;

import javax.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "lessons")
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private User student;

    @ManyToOne
    @JoinColumn(name = "tutor_id")
    private User tutor;

    @ManyToOne
    @JoinColumn(name = "time_slot_id", unique = true)
    private TimeSlot timeSlot;

    public Lesson(User student, User tutor, TimeSlot timeSlot) {
        this.student = student;
        this.tutor = tutor;
        this.timeSlot = timeSlot;
    }
}
