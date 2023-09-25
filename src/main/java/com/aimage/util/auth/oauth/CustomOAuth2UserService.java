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

import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    /**
     * 소셜 로그인을 위해 DB에 사용자 조회
     * 회원가입이 되어 있지 않은 경우, 회원가입 처리 후 로그인 진행
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        String provider = userRequest.getClientRegistration().getRegistrationId();
        OAuth2User oAuth2User = super.loadUser(userRequest);

        return new CustomUserDetails(registerUser(provider, oAuth2User), oAuth2User.getAttributes());
    }

    private User registerUser(String provider, OAuth2User oAuth2User) {
        String email = "";
        String username = "";
        String uuid = UUID.randomUUID().toString().substring(0, 16);
        String password = passwordEncoder.encode(uuid);

        if (provider.equals("kakao")) {
            KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
            email = kakaoUserInfo.getEmail();
            username = kakaoUserInfo.getNickname();
        } else {
            email = oAuth2User.getAttribute("email");
            username = email.split("@")[0];
        }

        // 소셜 계정의 email로 사용자 조회를 시도하고, 이미 존재하는 사용자일 경우, 해당 계정으로 로그인 하도록 예외 처리
        Optional<User> byEmail = userRepository.findByEmail(email);

        if (byEmail.isPresent()) {
            User user = byEmail.get();

            if (user.getProvider() == null) {
                throw new OAuth2AuthenticationException("이미 해당 이메일로 가입된 계정이 존재합니다.");
            } else {
                return user;
            }
        }

        User newUser = User.JoinOAuth2()
                .email(email)
                .username(username)
                .password(password)
                .provider(provider)
                .build();

        return userRepository.save(newUser);
    }
}
