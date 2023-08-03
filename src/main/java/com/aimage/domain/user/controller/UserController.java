package com.aimage.domain.user.controller;

import com.aimage.domain.image.entity.Image;
import com.aimage.domain.user.dto.UserDto;
import com.aimage.domain.user.service.UserService;
import com.aimage.domain.user.vo.UserVO;
import com.aimage.web.SessionConst;
import com.aimage.web.exception.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/signup")
    public String signupForm(@ModelAttribute UserDto.Signup signupDto) {
        return "user/signup-screen";
    }

    @GetMapping("/login")
    public String loginForm(@ModelAttribute UserDto.Login loginDto) {
        return "user/login-screen";
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            log.info("User signed out = {}", session.getAttribute(SessionConst.LOGIN_USER));
            session.invalidate();
        }

        return "redirect:/";
    }

    @GetMapping("/pwInquiry")
    public String findPasswordForm(@ModelAttribute UserDto.PwInquiry pwInquiry) {
        return "user/pwInquiry";
    }

    @GetMapping("/pwInquiry/{id}/newPw")
    public String updatePwForm(@PathVariable Long id, Model model) {
        model.addAttribute("userId", id);
        return "user/updatePw";
    }

    @GetMapping("/userInfo")
    public String userInfo(@SessionAttribute(required = false) UserVO loginUser) {
        return "user/userInfo";
    }

    @ResponseBody
    @PostMapping("/userInfo/editUser")
    public ResponseEntity<ErrorResponse> updateUsername(@SessionAttribute(required = false) UserVO loginUser,
                                                        @Validated @RequestBody UserDto.UpdateUsername updateUsername,
                                                        HttpServletRequest request) {

        log.info("updateUsername = {}", updateUsername.getUsername());
        String newUsername = userService.updateUsername(loginUser, updateUsername);

        HttpSession session = request.getSession(false);

        if (session != null) {
            session.setAttribute(SessionConst.LOGIN_USER, new UserVO(loginUser.getId(), newUsername, loginUser.getEmail()));
        }
        log.info("newUsername = {}", newUsername);
        return new ResponseEntity<>(new ErrorResponse(newUsername, "닉네임 변경이 완료되었습니다."), HttpStatus.OK);
    }


    @ResponseBody
    @PostMapping("/userInfo/editPw")
    public ResponseEntity<ErrorResponse> updatePassword(@SessionAttribute(required = false) UserVO loginUser,
                                                        @Valid @RequestBody UserDto.UpdatePassword updatePassword) {

        UserVO updateSucceeded = userService.updatePassword(loginUser.getId(), updatePassword);

//        if (!updateSucceeded) {
//            log.info("Field errors = {}", bindingResult.getFieldError("password").getDefaultMessage());
//            throw new AimageUserException("비밀번호 변경 실패");
//        }

        return new ResponseEntity<>(new ErrorResponse("ok", "비밀번호 변경이 완료되었습니다."), HttpStatus.OK);
    }

    @PostMapping("/userInfo/delete")
    public String deleteAccount(@SessionAttribute(required = false) UserVO loginUser,
                                HttpServletRequest request) {

        userService.deleteAccount(loginUser);
        HttpSession session = request.getSession(false);

        if (session != null)
            session.invalidate();

        return "redirect:/";
    }


    @GetMapping("/myGallery")
    public String myGallery(@SessionAttribute(required = false) UserVO loginUser, Model model) {
        List<Image> savedImages = userService.findSavedImages(loginUser.getId());
        model.addAttribute("savedImages", savedImages);

        return "user/myGallery";
    }
}
