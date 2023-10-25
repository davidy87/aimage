package com.aimage.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PageInfo {

    PAGE_SIZE(5);

    private final int size;
}
