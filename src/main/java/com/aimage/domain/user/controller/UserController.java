package com.aimage.domain.user.controller;

import com.aimage.domain.user.dto.UserDto;
import com.aimage.domain.user.entity.User;
import com.aimage.domain.user.service.UserServiceImpl;
import com.aimage.web.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;

    @GetMapping("/signup")
    public String signupForm(@ModelAttribute UserDto.Signup signupDto) {
        return "login/signup-screen";
    }

    @PostMapping("/signup")
    public String signup(@Validated @ModelAttribute UserDto.Signup signupDto,
                         BindingResult bindingResult) {

        if (invalidSignup(signupDto, bindingResult)) {
            return "login/signup-screen";
        }

        return "redirect:/";
    }

    /**
     * 회원가입 실패 여부
     */
    public boolean invalidSignup(UserDto.Signup signupDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return true;
        }

        User signupUser = userService.join(signupDto);

        // 비밀번호 재입력 불일치
        if (signupUser == null) {
            bindingResult.rejectValue("confirmPassword", "signup.confirmPassword");
            return true;
        }

        log.info("Sign up user = {}", signupUser.toString());
        return false;
    }

    @GetMapping("/login")
    public String loginForm(@ModelAttribute UserDto.Login loginDto) {
        return "login/login-screen";
    }

    @PostMapping("/login")
    public String login(@Validated @ModelAttribute UserDto.Login loginDto,
                        BindingResult bindingResult,
                        HttpServletRequest request) {

        User loginUser = userService.login(loginDto.getEmail(), loginDto.getPassword());

        if (invalidLogin(loginUser, bindingResult)) {
            return "login/login-screen";
        }

        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_USER, loginUser);

        log.info("Login user = {}", loginUser);
        return "redirect:/";
    }

    /**
     * 로그인 실패 여부 확인
     */
    private boolean invalidLogin(User loginUser, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return true;
        } else if (loginUser == null) {
            bindingResult.reject("login.fail");
            return true;
        }

        return false;
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
        return "login/pwInquiry";
    }

    @PostMapping("/pwInquiry")
    public String findPassword(@Validated @ModelAttribute UserDto.PwInquiry pwInquiry,
                               BindingResult bindingResult,
                               HttpServletRequest request) {

        User userFound = userService.findUserToResetPw(pwInquiry.getEmail());

        if (userFound == null) {
            bindingResult.reject("login.pwInquiry.failed");
            return "login/pwInquiry";
        }

        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.USER_TO_RESET_PW, userFound);

        return "redirect:/pwInquiry/updatePw";
    }

    @GetMapping("/pwInquiry/updatePw")
    public String updatePwForm(@ModelAttribute UserDto.UpdatePassword updatePassword) {
        return "login/updatePw";
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
                return "login/updatePw";
            }

            session.invalidate();
        }

        return "redirect:/login";
    }

}
