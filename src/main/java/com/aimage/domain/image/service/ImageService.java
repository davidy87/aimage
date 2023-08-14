package com.aimage.domain.image.service;

import com.aimage.domain.image.dto.ImageVO;
import com.aimage.domain.image.entity.Image;
import com.aimage.domain.image.repository.ImageRepository;
import com.aimage.domain.user.entity.User;
import com.aimage.domain.user.repository.UserRepository;
import com.aimage.web.exception.AimageUserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.aimage.domain.image.dto.ImageDto.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ImageService {

    private final ImageRepository imageRepository;

    private final UserRepository userRepository;

    private final OpenAiClientService openAiClientService;

    public ImageVO save(Long userId, ImageResult imageResult) {
        User owner = userRepository.findById(userId).orElseThrow(() ->
                new AimageUserException("이미지 저장에 실패했습니다."));

        Image image = Image.builder()
                .prompt(imageResult.getPrompt())
                .size(imageResult.getSize())
                .url(imageResult.getUrl())
                .build();

        Image imageSaved = imageRepository.save(image);
        owner.saveImage(imageSaved);

        return new ImageVO(imageSaved.getId(), imageSaved.getPrompt(), imageSaved.getUrl());
    }

    public ImageResult requestImageToOpenAI(ImageRequest imageRequest) {
        String imageUrl = openAiClientService.requestImage(imageRequest);
        return new ImageResult(imageRequest.getPrompt(), imageRequest.getSize(), imageUrl);
    }

    public void delete(Long ownerId, Long imageId) {
        User owner = userRepository.findById(ownerId).orElseThrow(() ->
                new AimageUserException("이미지를 삭제할 수 없습니다."));

        Image imageToDelete = imageRepository.findById(imageId).orElseThrow(() ->
                new AimageUserException("이미 존재하지 않는 이미지입니다."));

        owner.getImages().remove(imageToDelete);
        imageRepository.delete(imageToDelete);
    }

    // 테스트용
    public Optional<Image> findImageById(Long id) {
        return imageRepository.findById(id);
    }
}
