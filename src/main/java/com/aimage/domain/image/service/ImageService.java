package com.aimage.domain.image.service;

import com.aimage.domain.image.dto.ImageDto;
import com.aimage.domain.image.entity.Image;


public interface ImageService {

    Image save(Long userId, ImageDto.ImageRequest imageRequest, String imageURL);

    String requestImageToOpenAI(ImageDto.ImageRequest imageRequest);

    void delete(Long imageId);


}
