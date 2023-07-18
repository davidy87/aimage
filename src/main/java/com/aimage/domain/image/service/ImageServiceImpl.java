package com.aimage.domain.image.service;

import com.aimage.domain.image.dto.ImageDTO;
import com.aimage.domain.image.entity.Image;
import com.aimage.domain.image.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;

    @Override
    public void save(ImageDTO imageDTO, String imageURL) {
        Image image = Image.builder()
                .prompt(imageDTO.getPrompt())
                .size(imageDTO.getSize())
                .url(imageURL)
                .build();

        imageRepository.save(image);
    }

    @Override
    public Image findImage(Long imageId) {
        return null;
    }
}
