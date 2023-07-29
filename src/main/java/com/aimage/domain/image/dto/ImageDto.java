package com.aimage.domain.image.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.ToString;

import java.util.List;


public class ImageDto {

    @Getter
    @ToString
    public static class ImageRequest {

        @NotBlank(message = "{notEmpty}")
        private String prompt;

        @NotBlank(message = "{image.size.notEmpty}")
        private String size;

        // 테스트용
        public ImageRequest(String prompt, String size) {
            this.prompt = prompt;
            this.size = size;
        }
    }

    @Getter
    @ToString
    public static class ImageResponse {

        private List<ImageResponse> data;
        private String url;

    }

}
