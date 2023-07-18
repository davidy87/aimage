package com.aimage.domain.user.entity;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void builder() {
        User user = User.builder()
                .username("tester")
                .email("test@gmail.com")
                .password("testpass")
                .build();

        Assertions.assertThat(user.getId()).isEqualTo(null);
    }
}