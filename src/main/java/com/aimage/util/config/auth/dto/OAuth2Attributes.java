package com.aimage.util.config.auth.dto;

import com.aimage.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

import static com.aimage.enums.OAuthProvider.*;

@Getter
public class OAuth2Attributes {

    private Map<String, Object> attributes;

    private String nameAttributeKey;

    private String username;

    private String email;

    private String registrationId;

    @Builder
    public OAuth2Attributes(Map<String, Object> attributes, String nameAttributeKey, String username, String email, String registrationId) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.username = username;
        this.email = email;
        this.registrationId = registrationId;
    }

    public static OAuth2Attributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        if (registrationId.equals(NAVER.name())) {
            return ofNaver(NAVER.name(), userNameAttributeName, attributes);
        } else if (registrationId.equals(KAKAO.name())) {
            return ofKakao(KAKAO.name(), userNameAttributeName, attributes);
        }

        return ofGoogle(GOOGLE.name(), userNameAttributeName, attributes);
    }

    private static OAuth2Attributes ofGoogle(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        return OAuth2Attributes.builder()
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .username((String) attributes.get("given_name"))
                .email((String) attributes.get("email"))
                .registrationId(registrationId)
                .build();
    }

    private static OAuth2Attributes ofNaver(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuth2Attributes.builder()
                .attributes(response)
                .nameAttributeKey(userNameAttributeName)
                .username((String) response.get("nickname"))
                .email((String) response.get("email"))
                .registrationId(registrationId)
                .build();
    }

    private static OAuth2Attributes ofKakao(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");

        return OAuth2Attributes.builder()
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .username((String) properties.get("nickname"))
                .email((String) kakaoAccount.get("email"))
                .registrationId(registrationId)
                .build();
    }

    public User toEntity(String password) {
        return User.JoinOAuth2()
                .email(email)
                .username(username)
                .password(password)
                .provider(registrationId)
                .build();
    }
}
