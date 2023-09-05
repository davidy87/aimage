package com.aimage.domain.image.dto;

import com.aimage.domain.image.entity.Image;
import lombok.Getter;

@Getter
public class ImageVO {

    private final Long id;

    private final String prompt;

    private final String url;

    private final String owner;

    public ImageVO(Image image) {
        this.id = image.getId();
        this.prompt = image.getPrompt();
        this.url = image.getUrl();
        this.owner = image.getOwner().getUsername();
    }
}
