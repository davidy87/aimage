package com.aimage.util.config.auth.oauth;

import com.aimage.domain.user.entity.User;
import com.aimage.domain.user.repository.UserRepository;
import com.aimage.util.config.auth.CustomUserDetails;
import com.aimage.util.config.auth.dto.OAuth2Attributes;
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
        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration()
                .getRegistrationId()
                .toUpperCase();

        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        OAuth2Attributes attributes = OAuth2Attributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
        User user = saveOrUpdate(attributes, registrationId);

        return new CustomUserDetails(user, attributes);
    }

    private User saveOrUpdate(OAuth2Attributes attributes, String registrationId) {
        // 새로운 계정을 위한 비밀번호 생성
        String uuid = UUID.randomUUID().toString().substring(0, 16);
        String password = passwordEncoder.encode(uuid);

        User user = userRepository.findByEmail(attributes.getEmail())
                .filter(entity -> checkUserExistence(entity, registrationId))
                .map(entity -> entity.updateUsername(attributes.getUsername()))
                .orElse(attributes.toEntity(password));

        return userRepository.save(user);
    }

    private boolean checkUserExistence(User user, String provider) {
        if (user == null) {
            return false;
        } else if (!user.getProvider().equals(provider)) {
            throw new OAuth2AuthenticationException("이미 해당 이메일로 가입된 계정이 존재합니다.");
        }

        return true;
    }
}

