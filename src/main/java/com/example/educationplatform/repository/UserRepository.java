package com.example.educationplatform.repository;

import com.example.educationplatform.model.EnglishLevel;
import com.example.educationplatform.model.Role;
import com.example.educationplatform.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findAllByEmail(String email);
    Optional<User> findAllById(Long id);

    List<User> findAllByRoleIn(List<Role> roles);
    List<User> findAllByRole(Role role);
    List<User> findAllByRoleIn(List<Role> roles, Pageable pageable);
    List<User> findAllByRole(Role role, Pageable pageable);
    List<User> findByRole(Role role);
    List<User> findUsersByRoleAndEnglishLevelsIn(Role role, Collection<EnglishLevel> englishLevels, Pageable pageable);
}
