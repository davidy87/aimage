package com.aimage.domain.image.repository;

import com.aimage.domain.image.entity.Image;
import com.aimage.domain.user.entity.User;
import com.aimage.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.aimage.domain.image.entity.ImageSizeConst.*;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class ImageRepositoryTest {

    @Autowired
    ImageRepository imageRepository;
    @Autowired
    UserRepository userRepository;

    User imageOwner;

    /**
     * Create an image owner before each test
     */
    @BeforeEach
    void beforeEach() {
        imageOwner = User.builder()
                .username("imageOwner")
                .email("imageOwner@gmail.con")
                .password("testpass!")
                .build();

        userRepository.save(imageOwner);
    }

    @Test
    void save() {
        // Given
        Image image = Image.builder()
                .prompt("Test image")
                .size(SMALL)
                .url("Image.png")
                .build();

        // When
        Image imageSaved = imageRepository.save(image);
        imageOwner.saveImage(imageSaved);

        // Then
        Image imageFound = imageRepository.findById(image.getId()).get();
        assertThat(imageSaved).isEqualTo(imageFound);
        assertThat(imageOwner.getImages()).contains(imageFound);
    }

    @Test
    void findAll() {
        // Given
        Image[] images = getImages();

        for (Image image : images) {
            Image savedImage = imageRepository.save(image);
            imageOwner.saveImage(savedImage);
        }

        // When
        List<Image> foundImages = imageRepository.findAll();

        // Then
        assertThat(foundImages.size()).isEqualTo(images.length);
        assertThat(foundImages).contains(images);
        assertThat(imageOwner.getImages()).contains(images);
    }

    @Test
    void findAllBySize() {
        // Given
        Image[] images = getImages();

        for (Image image: images) {
            imageRepository.save(image);
            imageOwner.saveImage(image);
        }

        // When
        List<Image> imagesFound = imageRepository.findAllBySize(SMALL);

        // Then
        assertThat(imagesFound.size()).isEqualTo(2);
        assertThat(imageOwner.getImages()).containsAll(imagesFound);
    }

    @Test
    void findAllByUser() {
        // Given
        User anotherOwner = User.builder()
                .username("anotherOwner")
                .email("imageOwner@gmail.con")
                .password("testpass!")
                .build();

        userRepository.save(anotherOwner);

        Image[] images = getImages();

        for (int i = 0; i < images.length; i++) {
            Image savedImage = imageRepository.save(images[i]);

            if (i < images.length - 1) {
                imageOwner.saveImage(savedImage);
            }

            if (i == images.length - 1) {
                anotherOwner.saveImage(savedImage);
            }
        }

        // When
        List<Image> imagesFound1 = imageRepository.findAllByOwnerId(imageOwner.getId());
        List<Image> imagesFound2 = imageRepository.findAllByOwnerId(anotherOwner.getId());

        // Then
        assertThat(imagesFound1.size()).isEqualTo(2);
        assertThat(imagesFound1).contains(images[0], images[1]);
        assertThat(imageOwner.getImages()).contains(images[0], images[1]);

        assertThat(imagesFound2.size()).isEqualTo(1);
        assertThat(imagesFound2).contains(images[images.length - 1]);
        assertThat(anotherOwner.getImages()).contains(images[images.length - 1]);
    }

    @Test
    void delete() {
        // Given
        Image[] images = getImages();

        for (Image image : images) {
            imageOwner.saveImage(image);
            imageRepository.save(image);
        }

        // When
        Image imageToDelete = imageRepository.findById(1L).get();
        imageOwner.getImages().remove(imageToDelete);
        imageRepository.delete(imageToDelete);

        // Then
        assertThat(imageRepository.findById(1L)).isEmpty();
        assertThat(imageOwner.getImages()).doesNotContain(imageToDelete);
    }

    private Image[] getImages() {
        Image image1 = Image.builder()
                .prompt("Test image 1")
                .size(SMALL)
                .url("Image1.png")
                .build();

        Image image2 = Image.builder()
                .prompt("Test image 2")
                .size(LARGE)
                .url("Image2.png")
                .build();

        Image image3 = Image.builder()
                .prompt("Test image 3")
                .size(SMALL)
                .url("Image3.png")
                .build();

        return new Image[]{image1, image2, image3};
    }
}