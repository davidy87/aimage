package com.aimage.domain.image.repository;

import com.aimage.domain.image.entity.Image;
import com.aimage.domain.image.entity.ImageSizeConst;
import com.aimage.domain.user.entity.User;
import com.aimage.domain.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.aimage.domain.image.entity.ImageSizeConst.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class DbImageRepositoryTest {

    @Autowired
    ImageRepository imageRepository;
    @Autowired
    UserRepository userRepository;

    private User imageOwner;

    /**
     * Create an image owner before each test
     */
    @BeforeEach
    void beforeEach() {
        this.imageOwner = User.builder()
                .username("imageOwner")
                .email("imageOwner@gmail.con")
                .password("testpass!")
                .build();

        userRepository.save(this.imageOwner);
    }

    @Test
    void save() {
        // Given
        Image image = Image.builder()
                .ownerId(this.imageOwner.getId())
                .prompt("Test image")
                .size(SMALL)
                .url("Image.png")
                .build();

        // When
        Image imageSaved = imageRepository.save(image);

        // Then
        Image imageFound = imageRepository.findById(image.getId()).get();
        assertThat(imageSaved).isEqualTo(imageFound);
    }

    @Test
    void findAll() {
        // Given
        Image image1 = Image.builder()
                .ownerId(this.imageOwner.getId())
                .prompt("Test image 1")
                .size(SMALL)
                .url("Image1.png")
                .build();

        Image image2 = Image.builder()
                .ownerId(this.imageOwner.getId())
                .prompt("Test image 2")
                .size(LARGE)
                .url("Image2.png")
                .build();

        // When
        imageRepository.save(image1);
        imageRepository.save(image2);

        // Then
        assertThat(imageRepository.findAll().size()).isEqualTo(2);
        assertThat(imageRepository.findAll()).contains(image1, image2);
    }

    @Test
    void findAllBySize() {
        // Given
        Image image1 = Image.builder()
                .ownerId(this.imageOwner.getId())
                .prompt("Test image 1")
                .size(SMALL)
                .url("Image1.png")
                .build();

        Image image2 = Image.builder()
                .ownerId(this.imageOwner.getId())
                .prompt("Test image 2")
                .size(LARGE)
                .url("Image2.png")
                .build();

        Image image3 = Image.builder()
                .ownerId(this.imageOwner.getId())
                .prompt("Test image 3")
                .size(SMALL)
                .url("Image3.png")
                .build();

        // When
        imageRepository.save(image1);
        imageRepository.save(image2);
        imageRepository.save(image3);

        // Then
        List<Image> imagesFound = imageRepository.findAllBySize(SMALL);
        assertThat(imagesFound.size()).isEqualTo(2);
        assertThat(imagesFound).contains(image1, image3);
    }
}