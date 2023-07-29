package com.aimage.domain.image.service;

import com.aimage.domain.image.dto.ImageDto;


public interface ImageService {

    void save(Long userId, ImageDto.ImageRequest imageDTO, String imageURL);

    String requestImageToOpenAI(ImageDto.ImageRequest imageRequest);

}
