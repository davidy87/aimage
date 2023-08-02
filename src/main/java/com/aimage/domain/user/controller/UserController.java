package com.aimage.domain.user.controller;

import com.aimage.domain.image.entity.Image;
import com.aimage.domain.user.dto.UserDto;
import com.aimage.domain.user.entity.User;
import com.aimage.domain.user.service.UserService;
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
import org.springframework.validation.BindingResult;
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

    @PostMapping("/pwInquiry")
    public String findPassword(@Validated @ModelAttribute UserDto.PwInquiry pwInquiry,
                               BindingResult bindingResult,
                               HttpServletRequest request) {

        User userFound = userService.findUserToResetPw(pwInquiry.getEmail());

        if (userFound == null) {
            bindingResult.reject("login.pwInquiry.failed");
            return "user/pwInquiry";
        }

        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.USER_TO_RESET_PW, userFound);

        return "redirect:/pwInquiry/updatePw";
    }

    @GetMapping("/pwInquiry/updatePw")
    public String updatePwForm(@ModelAttribute UserDto.UpdatePassword updatePassword) {
        return "user/updatePw";
    }

    @RequestMapping("/pwInquiry/updatePw")
    public String updatePw(@Validated @ModelAttribute UserDto.UpdatePassword updatePassword,
                           BindingResult bindingResult,
                           HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if (session != null) {
            User userToResetPw = (User) session.getAttribute(SessionConst.USER_TO_RESET_PW);
            log.info("User to reset password = {}", userToResetPw);
            boolean updateOk = userService.updatePassword(userToResetPw, updatePassword);

            if (!updateOk) {
                bindingResult.reject("signup.confirmPassword");
                return "user/updatePw";
            }

            session.invalidate();
        }

        return "redirect:/login";
    }

    @GetMapping("/userInfo")
    public String userInfo(@SessionAttribute(required = false) User loginUser) {
        return "user/userInfo";
    }

    @ResponseBody
    @PostMapping("/userInfo/editUser")
    public ResponseEntity<ErrorResponse> updateUsername(@SessionAttribute(required = false) User loginUser,
                                                        @Validated @RequestBody UserDto.UpdateUsername updateUsername,
                                                        HttpServletRequest request) {

        log.info("updateUsername = {}", updateUsername.getUsername());
        boolean updateSucceeded = userService.updateUsername(loginUser, updateUsername);

        HttpSession session = request.getSession(false);

        if (session != null)
            session.setAttribute(SessionConst.LOGIN_USER, userService.login(loginUser.getEmail(), loginUser.getPassword()));

        return new ResponseEntity<>(new ErrorResponse(updateUsername.getUsername(), "닉네임 변경이 완료되었습니다."), HttpStatus.OK);
    }


    @ResponseBody
    @PostMapping("/userInfo/editPw")
    public ResponseEntity<ErrorResponse> updatePassword(@SessionAttribute(required = false) User loginUser,
                                                        @Valid @RequestBody UserDto.UpdatePassword updatePassword) {

        boolean updateSucceeded = userService.updatePassword(loginUser, updatePassword);

//        if (!updateSucceeded) {
//            log.info("Field errors = {}", bindingResult.getFieldError("password").getDefaultMessage());
//            throw new AimageUserException("비밀번호 변경 실패");
//        }

        return new ResponseEntity<>(new ErrorResponse("ok", "비밀번호 변경이 완료되었습니다."), HttpStatus.OK);
    }

    @PostMapping("/userInfo/delete")
    public String deleteAccount(@SessionAttribute(required = false) User loginUser,
                                HttpServletRequest request) {

        userService.deleteAccount(loginUser);
        HttpSession session = request.getSession(false);

        if (session != null)
            session.invalidate();

        return "redirect:/";
    }


    @GetMapping("/myGallery")
    public String myGallery(@SessionAttribute(required = false) User loginUser, Model model) {
        List<Image> savedImages = userService.findSavedImages(loginUser.getId());
        model.addAttribute("savedImages", savedImages);

        return "user/myGallery";
    }
}
