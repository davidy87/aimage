package com.aimage.domain.image.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.*;

import static com.aimage.domain.image.dto.ImageDto.*;

@Slf4j
@Service
public class OpenAiWebClientService implements OpenAiClientService {

    @Value("${openai-key}")
    private String OPENAI_KEY;

    // https://platform.openai.com/docs/api-reference/images/create
    @Override
    public InputStream requestImageInputStream(ImageRequest imageRequestForm) {
        String url = "https://api.openai.com";
        String apiKey = OPENAI_KEY;

        WebClient webClient = WebClient.builder()
                .baseUrl(url)
                .defaultHeader("Authorization", String.format("Bearer %s", OPENAI_KEY))
                .codecs(configure -> configure.defaultCodecs().maxInMemorySize(-1))
                .build();

        OpenAiResponse response = webClient.post()
                .uri("/v1/images/generations")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(imageRequestForm)
                .retrieve()
                .bodyToMono(OpenAiResponse.class)
                .map(OpenAiResponse::getData)
                .block()
                .get(0);

        return convertImage(response.getB64_json());
    }

    private InputStream convertImage(String base64) {
        log.info("--- In OpenAiWebClientService ---");
        log.info("Converting Base64 to ByteArrayInputSteam...");
        byte[] imageByteArray = Base64.decodeBase64(base64);

        return new ByteArrayInputStream(imageByteArray);
    }
}
