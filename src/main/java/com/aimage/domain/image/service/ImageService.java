package com.aimage.domain.image.service;

import com.aimage.domain.image.entity.Image;
import org.springframework.stereotype.Service;

@Service
public interface ImageService {

    void save(Image image);
    Image findImage(Long imageId);
}
