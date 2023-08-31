package com.aimage.domain.user.controller;

import com.aimage.domain.image.dto.ImageVO;
import com.aimage.domain.user.service.UserService;
import com.aimage.domain.user.dto.UserVO;
import com.aimage.constant.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.aimage.constant.SessionConst.*;
import static com.aimage.domain.user.dto.UserDto.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserApiController {

    private final UserService userService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public UserVO signup(@Validated @RequestBody Signup signupForm) {
        return userService.join(signupForm);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    public UserVO login(@RequestBody Login loginForm, HttpServletRequest request) {
        return userService.login(loginForm.getEmail(), loginForm.getPassword());
    }

    /**
     * 비밀번호 문의 전, 이메일로 사용자 인증
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/pw-inquiry")
    public UserVO identifyUser(@Validated @RequestBody PwInquiry pwInquiry) {
        return userService.findUserToResetPw(pwInquiry.getEmail());
    }

    /**
     * 계정 삭제
     */
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}")
    public String deleteAccount(@PathVariable Long id, HttpServletRequest request) {
        userService.deleteAccount(id);
        return "success";
    }

    /**
     * 닉네임 변경
     */
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}/new-username")
    public UserVO updateUsername(@PathVariable Long id,
                                 @Validated @RequestBody UpdateUsername updateUsername,
                                 HttpServletRequest request) {

        return userService.updateUsername(id, updateUsername);
    }

    /**
     * 비밀번호 변경
     */
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}/new-pw")
    public UserVO resetPw(@PathVariable Long id,
                          @Validated @RequestBody UpdatePassword updatePassword) {

        return userService.updatePassword(id, updatePassword);
    }

    /**
     * 사용자가 저장한 이미지 리스트
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}/images")
    public Page<ImageVO> getUserSavedImages(@PathVariable Long id, Pageable pageable) {
        return userService.findSavedImages(id, pageable);
    }
}
