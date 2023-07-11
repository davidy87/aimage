package com.aimage.domain.image;

import lombok.Data;

import java.util.concurrent.atomic.AtomicLong;

@Data
public class Image {

    private Long id;
    private String description;
    private String size;

}
