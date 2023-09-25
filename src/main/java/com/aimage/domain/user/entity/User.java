package com.aimage.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Entity
@Table(name = "Member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String email;

    private String password;

    private String provider;

    @Builder
    public User(Long id, String username, String email, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.provider = "none";
    }

    @Builder(builderClassName = "JoinOAuth2", builderMethodName = "JoinOAuth2")
    public User(Long id, String username, String email, String password, String provider) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.provider = provider;
    }

    public void updateUsername(String newUsername) {
        username = newUsername;
    }

    public void updatePassword(String newPassword) {
        password = newPassword;
    }
}
