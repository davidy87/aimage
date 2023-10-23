package com.aimage.domain.image.service;

import com.aimage.domain.image.entity.Image;
import com.aimage.domain.image.repository.ImageRepository;
import com.aimage.domain.user.entity.User;
import com.aimage.domain.user.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import static com.aimage.domain.image.dto.ImageDto.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ImageServiceTest {

    ImageService imageService;

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    UserRepository userRepository;

    User owner;

    @BeforeEach
    void userSignup() {
        imageService = new ImageService(imageRepository, userRepository, null);

        owner = User.builder()
                .username("tester")
                .email("test@email.com")
                .password("testpass!")
                .build();

        userRepository.save(owner);
    }

    @Test
    @Order(1)
    void save() {
        // Given
        GeneratedImage imageResult = new GeneratedImage(
                "Spring",
                "256x256",
                "image.png");

        // When
        imageService.save(owner.getId(), imageResult);

        // Then
        Image imageFound = imageRepository.findAllByOwnerId(owner.getId()).get(0);
        assertThat(imageFound.getPrompt()).isEqualTo(imageResult.getPrompt());
        assertThat(imageFound.getUrl()).isEqualTo(imageResult.getUrl());
        assertThat(imageFound.getOwner()).isEqualTo(owner);
    }

    @Test
    void findImageById() {
        // Given
        GeneratedImage imageResult = new GeneratedImage(
                "Spring",
                "256x256",
                "image.png");

        ImageResponse imageSaved = imageService.save(owner.getId(), imageResult);

        // When
        ImageResponse imageFound = imageService.findImageById(imageSaved.getId());

        // Then
        assertThat(imageFound.getId()).isEqualTo(imageSaved.getId());
        assertThat(imageFound.getPrompt()).isEqualTo(imageSaved.getPrompt());
        assertThat(imageFound.getUrl()).isEqualTo(imageSaved.getUrl());
        assertThat(imageFound.getOwner()).isEqualTo(imageSaved.getOwner());
    }

    @Test
    void findPagedImages() {
        // Given
        saveMultipleImages();
        Pageable pageable = PageRequest.of(0, 5);

        // When
        Page<ImageResponse> pagedImages = imageService.findPagedImages(pageable);

        // Then
        assertThat(pagedImages.getTotalElements()).isEqualTo(100);
        assertThat(pagedImages.getTotalPages()).isEqualTo(100 / 5);
    }

    private void saveMultipleImages() {
        for (int i = 0; i < 100; i++) {
            GeneratedImage imageGenerated = new GeneratedImage("Test image", "256x256", "image.png");
            imageService.save(owner.getId(), imageGenerated);
        }
    }

    @Test
    void delete() {
        // Given
        GeneratedImage imageResult = new GeneratedImage(
                "Spring",
                "256x256",
                "image.png");

        imageService.save(owner.getId(), imageResult);
        Image savedImage = imageRepository.findAllByOwnerId(owner.getId()).get(0);

        // When
        imageService.delete(savedImage.getId());

        // Then
        assertThat(userRepository.count()).isEqualTo(1);
        assertThat(imageRepository.count()).isEqualTo(0);
    }

}