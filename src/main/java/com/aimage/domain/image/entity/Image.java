package com.aimage.domain.image.entity;

import com.aimage.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String prompt;

    @Column(name = "IMAGE_SIZE")
    private String size;

    private String url;

    @ManyToOne
    private User owner;

    @Builder
    public Image(Long id, String prompt, String size, String url) {
        this.id = id;
        this.prompt = prompt;
        this.size = size;
        this.url = url;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
