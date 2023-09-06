package com.aimage.domain.image.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Page;

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
    public static class OpenAiResponse {

        private List<OpenAiResponse> data;

        private String url;
    }

    @Getter
    @ToString
    @RequiredArgsConstructor
    public static class ImageResult {

        private final String prompt;

        private final String size;

        private final String url;
    }

    @Getter
    public static class PagedImages {

        private final int pageStart;

        private final int pageEnd;

        private final Page<ImageVO> images;

        public PagedImages(Page<ImageVO> savedImages) {
            int number = savedImages.getNumber();
            int size = savedImages.getSize();
            int totalPage = savedImages.getTotalPages();

            pageStart = (int) Math.floor((double) number / size) * size + 1;
            pageEnd = Math.min(pageStart + size - 1, totalPage);
            this.images = savedImages;
        }
    }

}
