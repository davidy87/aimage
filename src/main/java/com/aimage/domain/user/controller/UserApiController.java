package com.aimage.domain.user.controller;

import com.aimage.domain.user.dto.UserDto;
import com.aimage.domain.user.service.UserService;
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

    @PostMapping("")
    public ResponseEntity<Long> signup(@Validated @RequestBody UserDto.Signup signupForm) {
        Long userId = userService.join(signupForm);
        return ResponseEntity.status(HttpStatus.OK).body(userId);
    }
}
