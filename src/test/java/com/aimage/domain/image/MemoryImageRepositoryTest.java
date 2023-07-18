package com.aimage.domain.image;

import com.aimage.domain.image.entity.Image;
import com.aimage.domain.image.repository.MemoryImageRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MemoryImageRepositoryTest {
    private MemoryImageRepository imageRepository = new MemoryImageRepository();

    @AfterEach
    void afterEach() {
        imageRepository.clearAll();
    }

    @Test
    void save() {
        // Given
        Image image = new Image("This is for test", "256x256");
        image.setUrl("http://foo.bar");

        // When
        imageRepository.save(image);

        // Then
        assertThat(imageRepository.findAll()).contains(image);
    }

    @Test
    void findById() {
        Image image1 = new Image("This is image1", "256x256");
        Image image2 = new Image("This is image2", "1024x1024");

        // When
        imageRepository.save(image1);
        imageRepository.save(image2);

        // Then
        Image result1 = imageRepository.findById(image1.getId()).get();
        Image result2 = imageRepository.findById(image2.getId()).get();
        assertThat(result1).isEqualTo(image1);
        assertThat(result2).isEqualTo(image2);
    }

    @Test
    void findAll() {
        Image image1 = new Image("This is image1", "256x256");
        Image image2 = new Image("This is image2", "1024x1024");

        // When
        imageRepository.save(image1);
        imageRepository.save(image2);

        // Then
        assertThat(imageRepository.findAll().size()).isEqualTo(2);
        assertThat(imageRepository.findAll()).contains(image1, image2);
    }

    @Test
    void findAllBySize() {
        Image image1 = new Image("This is image1", "256x256");
        Image image2 = new Image("This is image2", "1024x1024");
        Image image3 = new Image("This is image3", "256x256");

        // When
        imageRepository.save(image1);
        imageRepository.save(image2);
        imageRepository.save(image3);

        // Then
        assertThat(imageRepository.findAllBySize("256x256").size()).isEqualTo(2);
        assertThat(imageRepository.findAll()).contains(image1, image3);

        assertThat(imageRepository.findAllBySize("1024x1024").size()).isEqualTo(1);
        assertThat(imageRepository.findAll()).contains(image2);
    }

}