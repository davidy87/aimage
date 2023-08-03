package com.aimage.domain.user.controller;

import com.aimage.domain.user.dto.UserDto;
import com.aimage.domain.user.service.UserService;
import com.aimage.domain.user.vo.UserVO;
import com.aimage.web.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserApiController {

    private final UserService userService;

    @PostMapping("")
    public ResponseEntity<UserVO> signup(@Validated @RequestBody UserDto.Signup signupForm) {
        UserVO signupUser = userService.join(signupForm);
        return ResponseEntity.status(HttpStatus.OK).body(signupUser);
    }

    @PostMapping("/login")
    public ResponseEntity<UserVO> login(@RequestBody UserDto.Login loginForm, HttpServletRequest request) {
        UserVO loginUser = userService.login(loginForm.getEmail(), loginForm.getPassword());

        // 사용자 확인 성공 시, 세션에 저장
        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_USER, loginUser);

        return ResponseEntity.status(HttpStatus.OK).body(loginUser);
    }


    @PostMapping("/pwInquiry")
    public ResponseEntity<UserVO> findUserToResetPw(@Validated @RequestBody UserDto.PwInquiry pwInquiry) {
        UserVO userFound = userService.findUserToResetPw(pwInquiry.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body(userFound);
    }

    /**
     * 비밀번호 변경
     */
    @PutMapping("/{id}/newPw")
    public ResponseEntity<UserVO> resetPw(@PathVariable Long id,
                                          @Validated @RequestBody UserDto.UpdatePassword updatePassword) {

        UserVO updatedUser = userService.updatePassword(id, updatePassword);
        return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
    }

}
