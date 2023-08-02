package com.aimage.domain.user.controller;

import com.aimage.domain.user.dto.UserDto;
import com.aimage.domain.user.entity.User;
import com.aimage.domain.user.service.UserService;
import com.aimage.web.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserApiController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Validated @RequestBody UserDto.Signup signupForm) {
        Long userId = userService.join(signupForm);
        return ResponseEntity.status(HttpStatus.OK).body("ok");
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody UserDto.Login loginForm,
                                      HttpServletRequest request) {

        User loginUser = userService.login(loginForm.getEmail(), loginForm.getPassword());

        // 유저 확인 성공 시, 세션에 저장
        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_USER, loginUser);

        return ResponseEntity.status(HttpStatus.OK)
                .body(User.builder()
                        .id(loginUser.getId())
                        .username(loginUser.getUsername())
                        .build());
    }


}
