package com.aimage.util.auth.oauth;

import com.aimage.domain.user.entity.User;
import com.aimage.domain.user.repository.UserRepository;
import com.aimage.util.auth.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

import static com.aimage.enums.OAuthProvider.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    /**
     * 소셜 로그인을 위해 DB에 사용자 조회
     * 회원가입이 되어 있지 않은 경우, 회원가입 처리 후 로그인 진행
     */
    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        String provider = userRequest.getClientRegistration().getRegistrationId().toUpperCase();
        OAuth2User oauth2User = super.loadUser(userRequest);

        return new CustomUserDetails(registerUser(provider, oauth2User), oauth2User.getAttributes());
    }

    private User registerUser(String provider, OAuth2User oauth2User) {
        log.info("Provider = {}", provider);
        log.info("oAuth2User.getAttributes() = {}", oauth2User.getAttributes());

        OAuth2UserInfo oAuth2UserInfo = getOAuth2UserInfo(provider, oauth2User);

        String email = oAuth2UserInfo.getEmail();
        String username = oAuth2UserInfo.getNickname();
        String uuid = UUID.randomUUID().toString().substring(0, 16);
        String password = passwordEncoder.encode(uuid);

        // 소셜 계정의 email로 사용자 조회를 시도하고, 이미 존재하는 사용자일 경우, 해당 계정으로 로그인 하도록 처리
        User existingUser = userRepository.findByEmail(email).orElse(null);

        if (checkUserExistence(existingUser, provider)) {
            return existingUser;
        }

        User newUser = User.JoinOAuth2()
                .email(email)
                .username(username)
                .password(password)
                .provider(provider)
                .build();

        return userRepository.save(newUser);
    }

    private boolean checkUserExistence(User user, String provider) {
        if (user == null) {
            return false;
        } else if (!user.getProvider().equals(provider)) {
            throw new OAuth2AuthenticationException("이미 해당 이메일로 가입된 계정이 존재합니다.");
        }

        return true;
    }

    private OAuth2UserInfo getOAuth2UserInfo(String provider, OAuth2User oauth2User) {
        OAuth2UserInfo oauth2UserInfo = null;

        if (provider.equals(KAKAO.name())) {
            oauth2UserInfo = new KakaoOAuth2UserInfo(oauth2User.getAttributes());
        } else if (provider.equals(NAVER.name())) {
            oauth2UserInfo = new NaverOAuth2UserInfo(oauth2User.getAttributes());
        } else if (provider.equals(GOOGLE.name())) {
            oauth2UserInfo = new GoogleOAuth2UserInfo(oauth2User.getAttributes());
        }

        return oauth2UserInfo;
    }
}

