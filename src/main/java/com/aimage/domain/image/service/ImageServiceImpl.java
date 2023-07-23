package com.aimage.domain.image.service;

import com.aimage.domain.image.dto.ImageDTO;
import com.aimage.domain.image.entity.Image;
import com.aimage.domain.image.repository.ImageRepository;
import com.theokanning.openai.OpenAiHttpException;
import com.theokanning.openai.image.CreateImageRequest;
import com.theokanning.openai.service.OpenAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    @Value("${openai-key}")
    private String OPENAI_KEY;

    private final ImageRepository imageRepository;

    private final OpenAiClientService openAiClientService;

    @Override
    public void save(Long userId, ImageDTO imageRequestForm, String imageURL) {
        Image image = Image.builder()
                .ownerId(userId)
                .prompt(imageRequestForm.getPrompt())
                .size(imageRequestForm.getSize())
                .url(imageURL)
                .build();

        imageRepository.save(image);
    }

    @Override
    public String requestImageToOpenAI(ImageDTO imageRequestForm) {
        return openAiClientService.requestImage(imageRequestForm);
    }

}
