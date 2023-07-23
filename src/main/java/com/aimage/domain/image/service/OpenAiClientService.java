package com.aimage.domain.image.service;


import com.aimage.domain.image.dto.ImageDTO;
import org.springframework.beans.factory.annotation.Value;


public interface OpenAiClientService {

    public String requestImage(ImageDTO imageRequestForm);
}
