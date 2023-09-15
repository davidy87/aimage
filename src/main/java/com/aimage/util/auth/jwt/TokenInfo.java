package com.aimage.util.auth.jwt;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TokenInfo {

    private String grantType;

    private String accessToken;

    private String refreshToken;
}
