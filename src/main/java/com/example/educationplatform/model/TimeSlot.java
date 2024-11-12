package com.example.educationplatform.model;

import javax.persistence.*;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "time_slots")
public class TimeSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "this field cannot be void")
    @Column(nullable = false)
    private Date startTime;

    @NotNull(message = "this field cannot be void")
    @Column(nullable = false)
    private Date endTime;

    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "tutor_id")
    private User tutor;

    @OneToMany (mappedBy = "timeSlot")
    @ToString.Exclude
    private List<Lesson> lessons;

    @NotNull
    private Long price;

    private boolean bookedStatus;
}
