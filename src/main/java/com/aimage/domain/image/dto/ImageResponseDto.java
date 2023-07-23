package com.aimage.domain.image.dto;

import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Getter
@ToString
public class ImageResponseDto {

    private List<ImageResponseDto> data;
    private String url;
}
