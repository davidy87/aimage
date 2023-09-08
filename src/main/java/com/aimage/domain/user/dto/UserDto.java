package com.aimage.domain.user.dto;

import com.aimage.domain.user.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

public class UserDto {

    @Getter
    @ToString
    @AllArgsConstructor
    public static class Signup {

        @NotBlank(message = "{notEmpty}")
        private String username;

        @NotBlank(message = "{notEmpty}")
        @Email
        private String email;

        @NotBlank(message = "{notEmpty}")
        @Size(min = 8, max = 16, message = "{signup.password.size}")
        private String password;

        @NotBlank(message = "{notEmpty}")
        private String confirmPassword;

        public User convertToEntity(String encodedPassword) {
            return User.builder()
                    .username(username)
                    .email(email)
                    .password(encodedPassword)
                    .build();
        }
    }

    @Getter
    @ToString
    @AllArgsConstructor
    public static class Login {

        private String email;

        private String password;
    }

    @Getter
    @AllArgsConstructor
    public static class PwInquiry {

        @NotBlank(message = "{notEmpty}")
        private String email;

        public PwInquiry() {}
    }

    @Getter
    @ToString
    @AllArgsConstructor
    public static class UpdateUsername {

        @NotBlank(message = "{notEmpty}")
        private String username;

        public UpdateUsername() {}
    }

    @Getter
    @ToString
    @AllArgsConstructor
    public static class UpdatePassword {

        @NotBlank(message = "{notEmpty}")
        @Size(min = 8, max = 16, message = "{signup.password.size}")
        private String password;

        @NotBlank(message = "{notEmpty}")
        private String confirmPassword;
    }

    @Getter
    public static class UserResponse {

        private final Long id;

        private final String username;

        private final String email;

        public UserResponse(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.email = user.getEmail();
        }
    }
}
