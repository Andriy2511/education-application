package com.example.educationplatform.listener;

import com.example.educationplatform.constants.RoleConstants;
import com.example.educationplatform.model.Role;
import com.example.educationplatform.model.User;
import com.example.educationplatform.service.IRoleService;
import com.example.educationplatform.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class RoleInitializationListener implements ApplicationListener<ContextRefreshedEvent> {

    private final IRoleService roleService;
    private final IUserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RoleInitializationListener(IRoleService roleService, IUserService userService, PasswordEncoder passwordEncoder) {
        this.roleService = roleService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        initializeRoles();
        registerAdminIfAbsent();
    }

    private void initializeRoles() {
        if (roleService.getRoleByName(RoleConstants.ADMIN_ROLE) == null) {
            roleService.createRole(new Role(RoleConstants.ADMIN_ROLE));
        }
        if (roleService.getRoleByName(RoleConstants.STUDENT_ROLE)== null) {
            roleService.createRole(new Role(RoleConstants.STUDENT_ROLE));
        }
        if (roleService.getRoleByName(RoleConstants.TUTOR_ROLE)== null) {
            roleService.createRole(new Role(RoleConstants.TUTOR_ROLE));
        }
    }

    private void registerAdminIfAbsent(){
        Role role = roleService.getRoleByName(RoleConstants.ADMIN_ROLE);
        if(userService.findAllUsersByRole(role).isEmpty()) {
            User user = new User();
            user.setName("Admin");
            user.setSurname("Admin");
            user.setEmail("Admin@gmail.com");
            String encodedPassword = passwordEncoder.encode("Admin");
            user.setPassword(encodedPassword);
            user.setRole(role);
            userService.registerUser(user);
        }
    }
}
