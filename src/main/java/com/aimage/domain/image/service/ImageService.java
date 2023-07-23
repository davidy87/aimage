package com.aimage.domain.image.service;

import com.aimage.domain.image.dto.ImageDTO;


public interface ImageService {

    void save(Long userId, ImageDTO imageDTO, String imageURL);

    String requestImageToOpenAI(ImageDTO imageDTO);

}
