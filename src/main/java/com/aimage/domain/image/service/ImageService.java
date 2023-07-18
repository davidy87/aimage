package com.aimage.domain.image.service;

import com.aimage.domain.image.dto.ImageDTO;
import com.aimage.domain.image.entity.Image;
import org.springframework.stereotype.Service;

public interface ImageService {

    void save(ImageDTO imageDTO, String imageURL);
    Image findImage(Long imageId);
}
