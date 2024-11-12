package com.example.educationplatform.service.implementation;

import com.example.educationplatform.model.Role;
import com.example.educationplatform.model.User;
import com.example.educationplatform.repository.RoleRepository;
import com.example.educationplatform.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class RoleService implements IRoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> findRoleByName(String name){
        return Collections.singletonList(roleRepository.findByName(name).get());
    }

    @Override
    public List<Role> findAllRoleByName(String name){
        return roleRepository.findAllByName(name);
    }

    @Override
    public boolean isUserContainRole(User user, String roleName){
        boolean isContain;
        if(user != null) {
            isContain = user.getRole().getName().equals(roleName);
        } else {
            isContain = false;
        }
        return isContain;
    }

    @Override
    public Role getRoleByName(String name) {
        List<Role> roles = roleRepository.findAllByName(name);
        return roles.size() > 0 ? roles.get(0) : null;
    }

    @Override
    public Role createRole(Role role) {
        return roleRepository.save(role);
    }
}
