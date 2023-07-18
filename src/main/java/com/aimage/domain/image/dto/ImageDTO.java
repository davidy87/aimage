package com.aimage.domain.image.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ImageDTO {

    @NotNull
    @NotEmpty(message = "{notEmpty}")
    private String prompt;

    @NotNull
    @NotEmpty(message = "{image.size.notEmpty}")
    private String size;

    public ImageDTO(String prompt, String size) {
        this.prompt = prompt;
        this.size = size;
    }

}
