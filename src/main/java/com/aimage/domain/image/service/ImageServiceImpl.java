package com.aimage.domain.image.service;

import com.aimage.domain.image.dto.ImageDto;
import com.aimage.domain.image.entity.Image;
import com.aimage.domain.image.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;

    private final OpenAiClientService openAiClientService;

    @Override
    public void save(Long userId, ImageDto.ImageRequest imageRequest, String imageURL) {
        Image image = Image.builder()
                .ownerId(userId)
                .prompt(imageRequest.getPrompt())
                .size(imageRequest.getSize())
                .url(imageURL)
                .build();

        imageRepository.save(image);
    }

    @Override
    public String requestImageToOpenAI(ImageDto.ImageRequest imageRequest) {
        return openAiClientService.requestImage(imageRequest);
    }

}
