package com.aimage.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SessionAttribute {

    LOGIN_USER("loginUser");

    private final String name;
}
