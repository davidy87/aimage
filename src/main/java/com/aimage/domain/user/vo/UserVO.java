package com.aimage.domain.user.vo;

import lombok.Getter;

@Getter
public class UserVO {

    private final Long id;

    private final String username;

    private final String email;

    public UserVO(Long id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }
}
