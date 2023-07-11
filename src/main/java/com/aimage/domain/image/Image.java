package com.aimage.domain.image;

import lombok.Data;

import java.util.concurrent.atomic.AtomicLong;

@Data
public class Image {

    private Long id;
    private String prompt;
    private String size;
    private String url;

    public Image() {
    }

    public Image(String prompt, String size) {
        this.prompt = prompt;
        this.size = size;
    }
}
