package com.example.educationplatform.model;

import lombok.*;

import java.util.*;
import javax.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String surname;
    @NotNull(message = "this field cannot be void")
    @NotBlank(message = "this field cannot be void")
    @Column(nullable = false, unique=true)
    private String email;

    @NotNull(message = "this field cannot be void")
    @NotBlank(message = "this field cannot be void")
    @Column(nullable = false, length = 10000)
    private String password;

    private String description;

    private String photo;

    @OneToMany (fetch = FetchType.LAZY, mappedBy = "student")
    @ToString.Exclude
    private List<Review> studentReviews;

    @OneToMany (fetch = FetchType.LAZY, mappedBy = "tutor")
    @ToString.Exclude
    private List<Review> tutorReviews;

    @OneToMany (mappedBy = "student")
    @ToString.Exclude
    private List<Lesson> studentLessons;

    @OneToMany (mappedBy = "tutor")
    @ToString.Exclude
    private List<Lesson> tutorLessons;

    @OneToMany (mappedBy = "user")
    @ToString.Exclude
    private List<Message> messages;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE, mappedBy = "users")
    @ToString.Exclude
    private List<EnglishLevel> englishLevels;

    @OneToMany(fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<TimeSlot> timeSlots;

    @OneToMany(fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Exam> exams;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role.getName()));
        return authorities;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return email != null ? email.hashCode() : 0;
    }
}
