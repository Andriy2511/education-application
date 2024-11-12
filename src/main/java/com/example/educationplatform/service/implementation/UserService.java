package com.example.educationplatform.service.implementation;

import com.example.educationplatform.model.EnglishLevel;
import com.example.educationplatform.model.Role;
import com.example.educationplatform.model.User;
import com.example.educationplatform.repository.UserRepository;
import com.example.educationplatform.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final RoleService roleService;

    @Autowired
    public UserService(UserRepository userRepository, RoleService roleService){
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    @Override
    public User registerUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public boolean isUserExistCheckByEmail(String email) {
        return userRepository.findAllByEmail(email).isPresent();
    }

    @Override
    public User getUserByEmail(String email){
        return userRepository.findAllByEmail(email).get();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findAllById(id).get();
    }

    @Override
    public List<User> findAllUsersByRoles(List<Role> roleList) {return userRepository.findAllByRoleIn(roleList);}

    @Override
    public List<User> findAllUsersByRole(Role role) {
        return userRepository.findAllByRole(role);
    }

    @Override
    public Long getCountOfUsersByRole(Role role) {
        return (long) userRepository.findByRole(role).size();
    }

    @Override
    public List<User> findUsersByRoleWithPagination(Role role, int page, int pageSize) {
        return userRepository.findAllByRole(role, PageRequest.of(page, pageSize));
    }

    @Override
    public List<User> findAllUsersByRole(Role role, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return userRepository.findAllByRole(role, pageable);
    }

    @Override
    public List<User> findUsersByRoleAndEnglishLevels(Role role, List<EnglishLevel> englishLevels, int page, int pageSize){
        Pageable pageable = PageRequest.of(page, pageSize);
        return userRepository.findUsersByRoleAndEnglishLevelsIn(role, englishLevels, pageable);
    }
}
