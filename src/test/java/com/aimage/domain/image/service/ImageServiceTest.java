package com.aimage.domain.image.service;

import com.aimage.domain.image.dto.ImageDto;
import com.aimage.domain.image.dto.ImageVO;
import com.aimage.domain.image.entity.Image;
import com.aimage.domain.image.repository.ImageRepository;
import com.aimage.domain.user.dto.UserDto;
import com.aimage.domain.user.entity.User;
import com.aimage.domain.user.repository.UserRepository;
import com.aimage.domain.user.service.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
        ImageDto.ImageResult imageResult = new ImageDto.ImageResult(
                "Spring",
                "256x256",
                "image.png");

        // When
        imageService.save(owner.getId(), imageResult);

        // Then
        Image imageFound = imageRepository.findAllByOwnerId(owner.getId()).get(0);
        assertThat(imageFound.getId()).isEqualTo(1L);
        assertThat(imageFound.getPrompt()).isEqualTo(imageResult.getPrompt());
        assertThat(imageFound.getUrl()).isEqualTo(imageResult.getUrl());
        assertThat(owner.getImages()).contains(imageFound);
        assertThat(imageFound.getOwner()).isEqualTo(owner);
    }

    @Test
    @Order(2)
    void delete() {
        // Given
        ImageDto.ImageResult imageResult = new ImageDto.ImageResult(
                "Spring",
                "256x256",
                "image.png");

        imageService.save(owner.getId(), imageResult);
        Image savedImage = imageRepository.findAllByOwnerId(owner.getId()).get(0);

        // When
        imageService.delete(owner.getId(), savedImage.getId());

        // Then
        assertThat(owner.getImages()).isEmpty();
        assertThat(userRepository.count()).isEqualTo(1);
        assertThat(imageRepository.count()).isEqualTo(0);
    }

}