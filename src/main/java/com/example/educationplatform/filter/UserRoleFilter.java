package com.example.educationplatform.filter;

import java.io.IOException;
import java.util.NoSuchElementException;

import com.example.educationplatform.constants.RoleConstants;
import com.example.educationplatform.model.Role;
import com.example.educationplatform.model.User;
import com.example.educationplatform.service.IRoleService;
import com.example.educationplatform.service.IUserService;
import com.example.educationplatform.service.implementation.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

@Component
public class UserRoleFilter implements Filter{
    private final IUserService userService;
    private final IRoleService roleService;

    @Autowired
    public UserRoleFilter(UserService userService, IRoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            try {
                String userEmail = authentication.getName();
                User user = userService.getUserByEmail(userEmail);
                Role role = user.getRole();
                boolean isUnregistered = role == null;
                boolean isStudent = role.getName().equals(RoleConstants.STUDENT_ROLE);
                boolean isTutor = role.getName().equals(RoleConstants.TUTOR_ROLE);
                boolean isAdmin = role.getName().equals(RoleConstants.ADMIN_ROLE);
                request.setAttribute("isUnregistered", isUnregistered);
                request.setAttribute("isStudent", isStudent);
                request.setAttribute("isTutor", isTutor);
                request.setAttribute("isAdmin", isAdmin);
            } catch (NoSuchElementException | NullPointerException e){
                request.setAttribute("isUnregistered", true);
                request.setAttribute("isStudent", false);
                request.setAttribute("isTutor", false);
                request.setAttribute("isAdmin", false);
            }
        } else {
            request.setAttribute("isUnregistered", true);
            request.setAttribute("isStudent", false);
            request.setAttribute("isTutor", false);
            request.setAttribute("isAdmin", false);
        }
        chain.doFilter(request, response);
    }
}
