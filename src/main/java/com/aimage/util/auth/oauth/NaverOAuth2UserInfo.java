package com.aimage.util.auth.oauth;

import java.util.Map;

public class NaverOAuth2UserInfo implements OAuth2UserInfo {

    private final Map<String, Object> response;

    public NaverOAuth2UserInfo(Map<String, Object> attributes) {
        response = (Map<String, Object>) attributes.get("response");
    }

    @Override
    public String getEmail() {
        return (String) response.get("email");
    }

    @Override
    public String getNickname() {
        return (String) response.get("nickname");
    }
}
