package com.aimage.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ImageSize {

    SMALL("256x256"),
    MEDIUM("512x512"),
    LARGE("1024x1024");

    private final String size;
}
