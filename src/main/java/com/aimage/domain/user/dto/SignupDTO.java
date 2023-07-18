package com.aimage.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class SignupDTO {

    @NotNull
    @NotEmpty(message = "{notEmpty}")
    private String username;

    @NotNull
    @NotEmpty(message = "{notEmpty}")
    @Email
    private String email;

    @NotNull
    @NotEmpty(message = "{notEmpty}")
    @Size(min = 8, max = 16, message = "{signup.password.size}")
    private String password;

    @NotNull
    @NotEmpty(message = "{notEmpty}")
    private String confirmPassword;

    public SignupDTO(String username, String email, String password, String confirmPassword) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }
}
