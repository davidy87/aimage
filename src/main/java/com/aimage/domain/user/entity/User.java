package com.aimage.domain.user.entity;

import com.aimage.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "Member")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String provider;

    @Builder
    public User(Long id, String username, String email, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.provider = "NONE";
    }

    @Builder(builderClassName = "JoinOAuth2", builderMethodName = "JoinOAuth2")
    public User(Long id, String username, String email, String password, String provider) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.provider = provider;
    }

    public User updateUsername(String newUsername) {
        username = newUsername;
        return this;
    }

    public void updatePassword(String newPassword) {
        password = newPassword;
    }
}
