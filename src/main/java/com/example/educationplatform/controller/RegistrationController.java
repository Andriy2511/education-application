package com.example.educationplatform.controller;

import com.example.educationplatform.DTO.UserDTO;
import com.example.educationplatform.constants.RoleConstants;
import com.example.educationplatform.model.Role;
import com.example.educationplatform.model.User;
import com.example.educationplatform.service.IRoleService;
import com.example.educationplatform.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@Slf4j
public class RegistrationController {
    private final IUserService userService;
    private final IRoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationController(IUserService userService, IRoleService roleService, PasswordEncoder passwordEncoder){
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/registration")
    public String showRegistrationForm(Model model){
        model.addAttribute("userDTO", new UserDTO());
        return "registration";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("userDTO") @Valid UserDTO userDTO, BindingResult result){
        if(result.hasErrors()){
            return "registration";
        }

        try {
            if (userService.isUserExistCheckByEmail(userDTO.getEmail())) {
                log.info("A user with this email has already exists, username: {}", userDTO.getEmail());
                result.rejectValue("email", "error.duplicateUsername", "Користувач з таким емейлом уже існує");
                return "registration";
            }

            User user = userDTO.getUserFromDTO();
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user = userService.registerUser(user);

            Role role;
            if(userDTO.getIsTutor()){
                role = roleService.getRoleByName(RoleConstants.TUTOR_ROLE);
                user.setRole(role);
                user.setDescription(userDTO.getDescription());
                user.setPhoto(ImageController.addPhotoToFolderWithUniqueName(userDTO.getPhoto()));
            } else {
                role = roleService.getRoleByName(RoleConstants.STUDENT_ROLE);
                user.setRole(role);
            }
            userService.registerUser(user);

            return "redirect:login";
        } catch (Exception e){
            e.printStackTrace();
            log.error("Error while adding to the database: \n{}", e.getMessage());
            return "registration";
        }
    }
}
