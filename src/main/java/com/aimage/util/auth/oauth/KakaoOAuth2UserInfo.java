package com.aimage.util.auth.oauth;

import java.util.Map;

public class KakaoOAuth2UserInfo implements OAuth2UserInfo {

    private final Map<String, Object> properties;

    private final Map<String, Object> kakaoAccount;

    public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
        properties = (Map<String, Object>) attributes.get("properties");
        kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
    }

    @Override
    public String getEmail() {
        return (String) kakaoAccount.get("email");
    }

    @Override
    public String getNickname() {
        return (String) properties.get("nickname");
    }
}
