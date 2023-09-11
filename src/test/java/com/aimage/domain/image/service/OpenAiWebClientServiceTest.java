package com.aimage.domain.image.service;

import com.aimage.domain.image.dto.ImageDto;
import com.aimage.constant.ImageSizeConst;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OpenAiWebClientServiceTest {

    @Autowired
    private OpenAiWebClientService webClientService;

    @Test
    void get() {
        ImageDto.ImageRequest imageDTO = new ImageDto.ImageRequest("Spring Framework", ImageSizeConst.SMALL);
        webClientService.requestImage(imageDTO);
    }
}