package com.aimage.domain.user.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.concurrent.atomic.AtomicLong;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class User {

    private Long id;

    private String username;

    private String email;

    private String password;

    //    public void setId(Long id) {
//        this.id = id;
//    }
}
