package com.aimage.domain.user.service;

import com.aimage.domain.image.entity.Image;
import com.aimage.domain.image.repository.ImageRepository;
import com.aimage.domain.user.entity.User;
import com.aimage.domain.user.repository.UserRepository;
import com.aimage.util.auth.AuthModificationHandler;
import com.aimage.util.exception.AimageException;
import com.aimage.util.jwt.JwtTokenProvider;
import com.aimage.util.jwt.TokenInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static com.aimage.constant.PageConst.PAGE_SIZE;
import static com.aimage.domain.image.dto.ImageDto.*;
import static com.aimage.domain.user.dto.UserDto.*;
import static com.aimage.util.exception.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    private final ImageRepository imageRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthModificationHandler authModificationHandler;

    private final JwtTokenProvider jwtTokenProvider;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public UserResponse join(SignupRequest signupForm) {
        // 비밀번호 재입력 일치 확인
        if (!signupForm.getPassword().equals(signupForm.getConfirmPassword())) {
            throw new AimageException(CONFIRM_PASSWORD);
        }

        String encodedPassword = passwordEncoder.encode(signupForm.getPassword());
        User user = signupForm.convertToEntity(encodedPassword);
        userRepository.save(user);

        return new UserResponse(user);
    }

    public UserResponse login(LoginRequest loginForm) {
        log.info("--- In UserService (login) ---");

        User loginUser = userRepository.findByEmail(loginForm.getEmail())
                .filter(user -> user.getPassword().equals(loginForm.getPassword()))
                .orElseThrow(() -> new AimageException(LOGIN_ERROR));

        return new UserResponse(loginUser);
    }

    public TokenInfo loginWithToken(LoginRequest loginForm) {
//        User loginUser = userRepository.findByEmail(loginForm.getEmail())
//                .filter(user -> user.getPassword().equals(loginForm.getPassword()))
//                .orElseThrow(() -> new AimageException(LOGIN_ERROR));
//
//        log.info("User logging in: {}", loginUser.getEmail());

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginForm.getEmail(), loginForm.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        return jwtTokenProvider.generateToken(authentication);
    }

    public UserResponse findUserToResetPw(PasswordInquiry pwInquiry) {
        User userFound = userRepository.findByEmail(pwInquiry.getEmail())
                .orElseThrow(() -> new AimageException(PASSWORD_INQUIRE_FAILED));

        log.info("User found = {}", userFound);

        return new UserResponse(userFound);
    }

    public UserResponse updateUsername(Long id, UsernameUpdate usernameUpdate) {
        String newUsername = usernameUpdate.getUsername();
        User userToUpdate = userRepository.findById(id)
                .filter(user -> !user.getUsername().equals(newUsername))
                .orElseThrow(() -> new AimageException(USERNAME_UPDATE_FAILED));

        userToUpdate.updateUsername(newUsername);

        // 새로운 인증 생성 및 추가
        authModificationHandler.updateAuth(userToUpdate);

        return new UserResponse(userToUpdate);
    }

    public UserResponse updatePassword(Long userId, PasswordUpdate passwordUpdate) {
        String newPassword = passwordUpdate.getPassword();
        String confirmPassword = passwordUpdate.getConfirmPassword();

        User userToUpdate = userRepository.findById(userId)
                .orElseThrow(() -> new AimageException(PASSWORD_INQUIRE_FAILED));

        if (!newPassword.equals(confirmPassword)) {
            throw new AimageException(CONFIRM_PASSWORD);
        }

        userToUpdate.updatePassword(passwordEncoder.encode(newPassword));

        // 새로운 인증 생성 및 추가
        authModificationHandler.updateAuth(userToUpdate);

        return new UserResponse(userToUpdate);
    }

    public void deleteAccount(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AimageException(USER_ALREADY_NOT_EXIST));

        // Spring Security session 삭제
        authModificationHandler.expireSession(userId);
        userRepository.delete(user);
    }

    public Page<ImageResponse> findSavedImages(Long userId, Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, PAGE_SIZE, Sort.Direction.DESC, "id");

        return imageRepository.findAllByOwnerId(userId, pageable).map(ImageResponse::new);
    }

    public ImageResponse findByOwnerIdAndImageId(Long userId, Long imageId) {
        Image image = imageRepository.findByOwnerIdAndId(userId, imageId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));

        return new ImageResponse(image);
    }
}
