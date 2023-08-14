package com.aimage.domain.user.entity;

import com.aimage.domain.image.entity.Image;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity(name = "Member")
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String email;

    private String password;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    @Builder
    public User(Long id, String username, String email, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public void saveImage(Image image) {
        images.add(image);
        image.setOwner(this);
    }

    public void updateUsername(String newUsername) {
        username = newUsername;
    }

    public void updatePassword(String newPassword) {
        password = newPassword;
    }
}
