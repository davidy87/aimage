package com.aimage.domain.image.service;

import com.aimage.domain.image.dto.ImageDTO;
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
        ImageDTO imageDTO = new ImageDTO("Spring Framework", ImageSizeConst.SMALL);
        webClientService.requestImage(imageDTO);
    }
}