package com.aimage.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class LoginDTO {

    @NotNull
    @NotEmpty(message = "{notEmpty}")
    @Email
    private String email;

    @NotNull
    @NotEmpty(message = "{notEmpty}")
    private String password;

    public LoginDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
