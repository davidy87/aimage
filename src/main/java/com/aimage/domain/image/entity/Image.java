package com.aimage.domain.image.entity;

import lombok.Builder;
import lombok.Getter;

import java.util.concurrent.atomic.AtomicLong;

@Getter
@Builder
public class Image {

    private Long id;
    private String prompt;
    private String size;
    private String url;

    public void setId(Long id) {
        this.id = id;
    }
}
