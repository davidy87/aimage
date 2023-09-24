package com.aimage.util.auth.oauth;

import java.util.Map;

/**
 * {
 *  id=3036095804,
 *  connected_at=2023-09-24T16:38:30Z,
 *  properties={nickname=정웅},
 *  kakao_account={
 *      profile_nickname_needs_agreement=false,
 *      profile={nickname=정웅},
 *      has_email=true,
 *      email_needs_agreement=false,
 *      is_email_valid=true,
 *      is_email_verified=true,
 *      email=dyoon0807@gmail.com
 *  }
 * }
 */
public class KakaoUserInfo {

    private final Map<String, Object> properties;

    private final Map<String, Object> kakaoAccount;

    public KakaoUserInfo(Map<String, Object> attributes) {
        properties = (Map<String, Object>) attributes.get("properties");
        kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
    }

    public String getNickname() {
        return (String) properties.get("nickname");
    }

    public String getEmail() {
        return (String) kakaoAccount.get("email");
    }
}
