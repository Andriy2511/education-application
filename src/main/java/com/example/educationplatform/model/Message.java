package com.example.educationplatform.model;

import javax.persistence.*;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "this field cannot be void")
    @NotBlank(message = "this field cannot be void")
    @Column(nullable = false)
    private String text;

    @NotNull(message = "this field cannot be void")
    @Column(nullable = false)
    private Date messageDate;

    @NotNull(message = "this field cannot be void")
    @Column(nullable = false)
    private Long receiverId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
