package com.aimage.domain.image.service;


import com.aimage.domain.image.dto.ImageDto;


public interface OpenAiClientService {

    public String requestImage(ImageDto imageRequestForm);
}
