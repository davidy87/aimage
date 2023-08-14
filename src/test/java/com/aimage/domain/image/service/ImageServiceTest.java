package com.aimage.domain.image.service;

import com.aimage.domain.image.dto.ImageDto;
import com.aimage.domain.image.dto.ImageVO;
import com.aimage.domain.image.entity.Image;
import com.aimage.domain.image.repository.ImageRepository;
import com.aimage.domain.user.dto.UserDto;
import com.aimage.domain.user.entity.User;
import com.aimage.domain.user.repository.UserRepository;
import com.aimage.domain.user.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class ImageServiceTest {

    @Autowired
    ImageService imageService;

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void userSignup() {
        User user = User.builder()
                .username("tester")
                .email("test@email.com")
                .password("testpass!")
                .build();

        userRepository.save(user);
    }

    @Test
    void save() {
        // Given
        User owner = userRepository.findById(1L).get();
        ImageDto.ImageResult imageResult = new ImageDto.ImageResult(
                "Spring",
                "256x256",
                "image.png");

        // When
        ImageVO savedImage = imageService.save(owner.getId(), imageResult);

        // Then
        Image imageFound = imageService.findImageById(savedImage.id()).get();
        assertThat(imageFound.getId()).isEqualTo(1L);
        assertThat(imageFound.getPrompt()).isEqualTo(imageResult.getPrompt());
        assertThat(imageFound.getUrl()).isEqualTo(imageResult.getUrl());
        assertThat(owner.getImages()).contains(imageFound);
        assertThat(imageFound.getOwner()).isEqualTo(owner);
    }

    @Test
    void delete() {
        // Given
        User owner = userRepository.findById(1L).get();
        ImageDto.ImageResult imageResult = new ImageDto.ImageResult(
                "Spring",
                "256x256",
                "image.png");

        ImageVO savedImage = imageService.save(owner.getId(), imageResult);

        // When
        imageService.delete(owner.getId(), savedImage.id());

        // Then
        Optional<Image> imageFound = imageService.findImageById(1L);
        assertThat(imageFound).isEmpty();
        assertThat(owner.getImages()).isEmpty();
        assertThat(userRepository.count()).isEqualTo(1);
        assertThat(imageRepository.count()).isEqualTo(0);
    }
}