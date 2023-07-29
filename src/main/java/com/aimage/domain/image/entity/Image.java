package com.aimage.domain.image.entity;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.concurrent.atomic.AtomicLong;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class Image {

    private Long id;

    private Long ownerId;

    private String prompt;

    private String size;

    private String url;

}
