package com.aimage.domain.user.entity;

import com.aimage.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "Member")
public class User extends BaseTimeEntity {

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
