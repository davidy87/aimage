package com.aimage.util.auth.oauth;

import java.util.Map;

public class NaverUserInfo {

    private final Map<String, Object> response;

    public NaverUserInfo(Map<String, Object> attributes) {
        response = (Map<String, Object>) attributes.get("response");
    }

    public String getEmail() {
        return (String) response.get("email");
    }

    public String getNickname() {
        return (String) response.get("nickname");
    }
}
