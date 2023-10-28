package com.aimage.domain.image.service;

import java.io.InputStream;

import static com.aimage.domain.image.dto.ImageDto.*;

public interface OpenAiClientService {

    InputStream requestImageInputStream(ImageRequest imageRequestForm);
}
