package com.aimage.domain.user.dto;

import com.aimage.domain.user.entity.User;
import lombok.Getter;

@Getter
public class UserVO {

    private final Long id;

    private final String username;

    private final String email;

    public UserVO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
    }
}
