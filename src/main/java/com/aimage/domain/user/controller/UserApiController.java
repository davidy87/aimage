package com.aimage.domain.user.controller;

import com.aimage.domain.user.service.UserService;
import com.aimage.util.auth.jwt.TokenInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.aimage.domain.image.dto.ImageDto.*;
import static com.aimage.domain.user.dto.UserDto.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserApiController {

    private final UserService userService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public UserResponse signup(@Validated @RequestBody SignupRequest signupForm) {
        return userService.join(signupForm);
    }

    @PostMapping("/login")
    public UserResponse login(@RequestBody LoginRequest loginForm) {
        return userService.login(loginForm);
    }

    @PostMapping("/jwt-login")
    public TokenInfo loginWithToken(@RequestBody LoginRequest loginForm) {
        return userService.loginWithToken(loginForm);
    }

    /**
     * 비밀번호 문의 전, 이메일로 사용자 인증
     */
    @GetMapping("/pw-inquiry")
    public UserResponse identifyUser(@Validated @RequestBody PasswordInquiry passwordInquiry) {
        return userService.findUserToResetPw(passwordInquiry);
    }

    /**
     * 계정 삭제
     */
    @DeleteMapping("/{id}")
    public String deleteAccount(@PathVariable Long id) {
        userService.deleteAccount(id);
        return "success";
    }

    /**
     * 닉네임 변경
     */
    @PutMapping("/{id}/new-username")
    public UserResponse updateUsername(@PathVariable Long id,
                                       @Validated @RequestBody UsernameUpdate usernameUpdate) {

        return userService.updateUsername(id, usernameUpdate);
    }

    /**
     * 비밀번호 변경
     */
    @PutMapping("/{id}/new-pw")
    public UserResponse resetPw(@PathVariable Long id,
                                @Validated @RequestBody PasswordUpdate passwordUpdate) {

        return userService.updatePassword(id, passwordUpdate);
    }

    /**
     * 사용자가 저장한 이미지 리스트
     */
    @GetMapping("/{id}/images")
    public Page<ImageResponse> getUserSavedImages(@PathVariable Long id, Pageable pageable) {
        return userService.findSavedImages(id, pageable);
    }
}
