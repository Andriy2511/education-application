package com.example.educationplatform.configuration.security;

import com.example.educationplatform.constants.RoleConstants;
import com.example.educationplatform.service.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailService userDetailService;
    private final AccessDeniedHandler myAccessDeniedHandler;

    @Autowired
    public SecurityConfig(CustomUserDetailService userDetailService, @Qualifier("myAccessDeniedHandler") AccessDeniedHandler myAccessDeniedHandler) {
        this.userDetailService = userDetailService;
        this.myAccessDeniedHandler = myAccessDeniedHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeRequests()
                .antMatchers("/student/**").hasAuthority(RoleConstants.STUDENT_ROLE)
                .antMatchers("/tutor/**").hasAuthority(RoleConstants.TUTOR_ROLE)
                .antMatchers("/admin/**").hasAuthority(RoleConstants.ADMIN_ROLE)
                .antMatchers("/message/**").hasAnyAuthority(RoleConstants.STUDENT_ROLE, RoleConstants.TUTOR_ROLE, RoleConstants.ADMIN_ROLE)
                .antMatchers("/test/**").hasAnyAuthority(RoleConstants.STUDENT_ROLE, RoleConstants.TUTOR_ROLE, RoleConstants.ADMIN_ROLE)
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage("/login")
                .usernameParameter("login")
                .and()
                .exceptionHandling()
                .accessDeniedHandler(myAccessDeniedHandler)
                .and()
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}

