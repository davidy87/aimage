package com.aimage.domain.image.dto;

import lombok.Builder;
import lombok.Getter;

public record ImageVO(Long id, String prompt, String url, String ownerName) {}
