package com.example.educationplatform.DTO;

import com.example.educationplatform.model.User;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    @NotBlank(message = "Name cannot be void")
    private String name;

    @NotBlank(message = "Surname cannot be void")
    private String surname;

    @NotBlank(message = "Login cannot be void")
    private String email;
    private String password;

    private Boolean isTutor;
    private String description;

    private MultipartFile photo;

    public User getUserFromDTO(){
        User user = new User();
        user.setName(name);
        user.setSurname(surname);
        user.setEmail(email);
        user.setPassword(password);

        return user;
    }
}
