package com.aimage.domain.image.service;

import com.aimage.domain.image.dto.ImageDto;
import com.aimage.domain.image.entity.ImageSizeConst;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OpenAiWebClientServiceTest {

    @Autowired
    private OpenAiWebClientService webClientService;

    @Test
    void get() {
        ImageDto imageDTO = new ImageDto("Spring Framework", ImageSizeConst.SMALL);
        webClientService.requestImage(imageDTO);
    }
}