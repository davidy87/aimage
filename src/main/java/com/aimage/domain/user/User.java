package com.aimage.domain.user;

import lombok.Data;

import java.util.concurrent.atomic.AtomicLong;

@Data
public class User {

    private Long id;
    private String username;
    private String email;
    private String password;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
