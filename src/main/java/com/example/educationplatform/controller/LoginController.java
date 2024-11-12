package com.example.educationplatform.controller;

import com.example.educationplatform.constants.RoleConstants;
import com.example.educationplatform.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Collection;

@Controller
public class LoginController {
    private final AuthenticationManager authenticationManager;

    @Autowired
    public LoginController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/authorize")
    public String loginUser(@ModelAttribute("user") @Valid User user, BindingResult result) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return getPageByRole(authentication);
        } catch (AuthenticationException e) {
            result.rejectValue("email", "error.invalidCredentials", "Invalid email or password");
            return "login";
        }
    }

    private String getPageByRole(Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if (authorities.stream().anyMatch(auth -> auth.getAuthority().equals(RoleConstants.STUDENT_ROLE))) {
            return "redirect:/catalog/showTutorsCatalog";
        } else if (authorities.stream().anyMatch(auth -> auth.getAuthority().equals(RoleConstants.TUTOR_ROLE))) {
            return "redirect:/tutor/openAddTimeSlotPage";
        } else if (authorities.stream().anyMatch(auth -> auth.getAuthority().equals(RoleConstants.ADMIN_ROLE))) {
            return "redirect:/admin/showCompletedTests";
        } else {
            return "redirect:/registration";
        }
    }
}
