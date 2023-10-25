package com.aimage.domain.image.entity;

import com.aimage.domain.BaseTimeEntity;
import com.aimage.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String prompt;

    @Column(name = "IMAGE_SIZE", nullable = false)
    private String size;

    @Column(nullable = false, length = 500)
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OWNER_ID")
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
