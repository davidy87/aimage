package com.aimage.domain.image.service;

import com.aimage.domain.image.dto.ImageDto;
import com.aimage.domain.image.entity.Image;
import com.aimage.domain.user.dto.UserDto;
import com.aimage.domain.user.entity.User;
import com.aimage.domain.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
@Transactional
class ImageServiceImplTest {

    @Autowired
    ImageService imageService;

    @Autowired
    UserService userService;


    @Test
    void delete() {
        // Given
        UserDto.Signup signup = new UserDto.Signup(
                "tester",
                "test@email.com",
                "testpass",
                "testpass");
        Long userId = userService.join(signup).getId();

        ImageDto.ImageRequest imageRequest = new ImageDto.ImageRequest("Spring", "256x256");
        Image savedImage = imageService.save(userId, imageRequest, "image.png");

        // When
        imageService.delete(savedImage.getId());

        // Then
        Optional<Image> foundImage = ((ImageServiceImpl) imageService).findImageById(savedImage.getId());
        assertThat(foundImage).isEmpty();
    }
}