package com.aimage.domain.image.entity;

import com.aimage.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.concurrent.atomic.AtomicLong;

@Getter
@ToString
@EqualsAndHashCode
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long ownerId;

    private String prompt;

    private String size;

    private String url;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Image(Long id, Long ownerId, String prompt, String size, String url) {
        this.id = id;
        this.ownerId = ownerId;
        this.prompt = prompt;
        this.size = size;
        this.url = url;
    }
}
