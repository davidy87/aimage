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
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String email = oAuth2User.getAttribute("email");

        // 소셜 계정의 email로 사용자 조회를 시도하고, 이미 존재하는 사용자일 경우, 해당 계정으로 로그인 하도록 예외 처리
        userRepository.findByEmail(email)
                .ifPresent(user -> {
                    throw new OAuth2AuthenticationException("이미 해당 이메일로 가입된 계정이 존재합니다.");
                });

        String username = email.split("@")[0];
        String uuid = UUID.randomUUID().toString().substring(0, 16);
        String password = passwordEncoder.encode(uuid);

        User newUser = User.builder()
                .email(email)
                .username(username)
                .password(password)
                .build();

        userRepository.save(newUser);

        return new CustomUserDetails(newUser, oAuth2User.getAttributes());
    }
}
