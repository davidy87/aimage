package com.aimage.domain.image.service;

import com.aimage.domain.image.dto.ImageVO;
import com.aimage.domain.image.entity.Image;
import com.aimage.domain.image.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.aimage.domain.image.dto.ImageDto.*;

@Service
@RequiredArgsConstructor
@Transactional
public class ImageService {

    private final ImageRepository imageRepository;

    private final OpenAiClientService openAiClientService;

    public ImageVO save(Long userId, ImageResult imageResult) {
        Image image = Image.builder()
                .ownerId(userId)
                .prompt(imageResult.getPrompt())
                .size(imageResult.getSize())
                .url(imageResult.getUrl())
                .build();

        imageRepository.save(image);

        return new ImageVO(image.getId(), imageResult.getPrompt(), imageResult.getUrl());
    }

    public ImageResult requestImageToOpenAI(ImageRequest imageRequest) {
        String imageUrl = openAiClientService.requestImage(imageRequest);
        return new ImageResult(imageRequest.getPrompt(), imageRequest.getSize(), imageUrl);
    }

    public void delete(Long imageId) {
        imageRepository.delete(imageId);
    }

    // 테스트용
    public Optional<Image> findImageById(Long id) {
        return imageRepository.findById(id);
    }
}
