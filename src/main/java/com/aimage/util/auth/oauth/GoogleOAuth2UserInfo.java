package com.aimage.util.auth.oauth;

import java.util.Map;

public class GoogleOAuth2UserInfo implements OAuth2UserInfo {

    private final String email;

    private final String nickname;

    public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
        email = (String) attributes.get("email");
        nickname = (String) attributes.get("given_name");
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getNickname() {
        return nickname;
    }
}
