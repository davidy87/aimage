package com.aimage.domain.image.service;

import com.aimage.domain.image.dto.ImageDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import static com.aimage.domain.image.dto.ImageDto.*;

@Slf4j
@Service
public class OpenAiWebClientService implements OpenAiClientService {

    @Value("${openai-key}")
    private String OPENAI_KEY;

    // https://platform.openai.com/docs/api-reference/images/create
    public String requestImage(ImageDto.ImageRequest imageRequestForm) {
        String url = "https://api.openai.com";
        String apiKey = OPENAI_KEY;

        WebClient webClient = WebClient.builder()
                .baseUrl(url)
                .defaultHeader("Authorization", String.format("Bearer %s", OPENAI_KEY))
                .build();

        // Expected: List<ImageDto.ImageResponse>
        ImageDto.ImageResponse response = webClient.post()
                .uri("/v1/images/generations")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(imageRequestForm)
                .retrieve()
                .bodyToMono(ImageDto.ImageResponse.class)
                .map(ImageDto.ImageResponse::getData)
                .block()
                .get(0);

        log.info("Image url = {}", response.getUrl());
        return response.getUrl();
    }
}
