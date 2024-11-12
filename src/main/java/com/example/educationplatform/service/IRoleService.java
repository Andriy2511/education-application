package com.example.educationplatform.service;

import com.example.educationplatform.model.Role;
import com.example.educationplatform.model.User;

import java.util.List;

public interface IRoleService {

    List<Role> findRoleByName(String name);

    List<Role> findAllRoleByName(String name);

    boolean isUserContainRole(User user, String roleName);
    Role getRoleByName(String name);

    Role createRole(Role role);
}
