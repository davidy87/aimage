package com.aimage.domain.image.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ImageDto {

    @NotBlank(message = "{notEmpty}")
    private String prompt;

    @NotBlank(message = "{image.size.notEmpty}")
    private String size;

    public ImageDto(String prompt, String size) {
        this.prompt = prompt;
        this.size = size;
    }

}
