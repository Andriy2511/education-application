package com.example.educationplatform.service;

import com.example.educationplatform.model.EnglishLevel;
import com.example.educationplatform.model.Role;
import com.example.educationplatform.model.User;

import java.util.List;

public interface IUserService {
    User registerUser(User user);

    boolean isUserExistCheckByEmail(String email);

    User getUserByEmail(String email);

    User getUserById(Long id);

    List<User> findAllUsersByRoles(List<Role> roleList);

    List<User> findAllUsersByRole(Role role);

    Long getCountOfUsersByRole(Role role);

    List<User> findUsersByRoleWithPagination(Role role, int page, int pageSize);

    List<User> findAllUsersByRole(Role role, int page, int pageSize);

    List<User> findUsersByRoleAndEnglishLevels(Role role, List<EnglishLevel> englishLevels, int page, int pageSize);
}
