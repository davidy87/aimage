package com.aimage.util.jwt;

import lombok.Builder;

@Builder
public class TokenInfo {

    private String grantType;

    private String accessToken;

    private String refreshToken;
}
