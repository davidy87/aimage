package com.aimage.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class UserVO {

    @Schema(example = "1")
    private final Long id;

    @Schema(example = "tester")
    private final String username;

    @Schema(example = "test@gmail.com")
    private final String email;

    public UserVO(Long id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }
}
