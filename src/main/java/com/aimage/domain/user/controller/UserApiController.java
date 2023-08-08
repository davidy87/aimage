package com.aimage.domain.user.controller;

import com.aimage.domain.image.entity.Image;
import com.aimage.domain.user.service.UserService;
import com.aimage.domain.user.dto.UserVO;
import com.aimage.web.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.aimage.domain.user.dto.UserDto.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserApiController {

    private final UserService userService;

    @PostMapping("")
    public ResponseEntity<UserVO> signup(@Validated @RequestBody Signup signupForm) {
        UserVO signupUser = userService.join(signupForm);
        return ResponseEntity.status(HttpStatus.OK).body(signupUser);
    }

    @PostMapping("/login")
    public ResponseEntity<UserVO> login(@RequestBody Login loginForm, HttpServletRequest request) {
        UserVO loginUser = userService.login(loginForm.getEmail(), loginForm.getPassword());

        // 사용자 확인 성공 시, 세션에 저장
        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_USER, loginUser);

        return ResponseEntity.status(HttpStatus.OK).body(loginUser);
    }

    /**
     * 비밀번호 문의 전, 이메일로 사용자 인증
     */
    @GetMapping("/pw-inquiry")
    public ResponseEntity<UserVO> identifyUser(@Validated @RequestBody PwInquiry pwInquiry) {
        UserVO userFound = userService.findUserToResetPw(pwInquiry.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body(userFound);
    }

    /**
     * 닉네임 변경
     */
    @ResponseBody
    @PutMapping("/{id}/new-username")
    public ResponseEntity<UserVO> updateUsername(@PathVariable Long id,
                                                 @Validated @RequestBody UpdateUsername updateUsername,
                                                 HttpServletRequest request) {

        UserVO updatedUser = userService.updateUsername(id, updateUsername);
        HttpSession session = request.getSession(false);

        if (session != null) {
            session.setAttribute(SessionConst.LOGIN_USER, updatedUser);
        }

        return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
    }

    /**
     * 비밀번호 변경
     */
    @PutMapping("/{id}/new-pw")
    public ResponseEntity<UserVO> resetPw(@PathVariable Long id,
                                          @Validated @RequestBody UpdatePassword updatePassword) {

        UserVO updatedUser = userService.updatePassword(id, updatePassword);
        return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
    }


    /**
     * 계정 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAccount(@PathVariable Long id, HttpServletRequest request) {
        userService.deleteAccount(id);
        HttpSession session = request.getSession(false);

        if (session != null)
            session.invalidate();

        return ResponseEntity.status(HttpStatus.OK).body("success");
    }

    /**
     * 사용자가 저장한 이미지 리스트
     */
    @GetMapping("/{id}/images")
    public ResponseEntity getUserSavedImages(@PathVariable Long id) {
        List<Image> savedImages = userService.findSavedImages(id);
        return ResponseEntity.status(HttpStatus.OK).body(savedImages);
    }
}
