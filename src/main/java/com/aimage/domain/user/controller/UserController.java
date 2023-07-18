package com.aimage.domain.user.controller;

import com.aimage.domain.user.dto.LoginDTO;
import com.aimage.domain.user.dto.SignupDTO;
import com.aimage.domain.user.entity.User;
import com.aimage.domain.user.service.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.naming.Binding;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;

    @GetMapping("/signup")
    public String signupForm(@ModelAttribute SignupDTO signupDTO) {
        return "login/signup-screen";
    }

    @PostMapping("/signup")
    public String signup(@Validated @ModelAttribute SignupDTO signupDTO, BindingResult bindingResult) {
        if (invalidSignup(signupDTO, bindingResult)) {
            return "login/signup-screen";
        }

        return "redirect:/";
    }

    /**
     * 회원가입 실패 여부
     */
    public boolean invalidSignup(SignupDTO signupDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return true;
        }

        User signupUser = userService.join(signupDTO);

        // 비밀번호 재입력 불일치
        if (signupUser == null) {
            bindingResult.rejectValue("confirmPassword", "signup.confirmPassword");
            return true;
        }

        log.info("Sign up user = {}", signupUser.toString());
        return false;
    }

    @GetMapping("/login")
    public String loginForm(@ModelAttribute LoginDTO loginDTO) {
        return "login/login-screen";
    }

    @PostMapping("/login")
    public String login(@Validated @ModelAttribute LoginDTO loginDTO,
                        BindingResult bindingResult,
                        HttpServletRequest request) {

        User loginUser = userService.login(loginDTO.getEmail(), loginDTO.getPassword());

        if (invalidLogin(loginUser, bindingResult)) {
            return "login/login-screen";
        }

        HttpSession session = request.getSession();
        session.setAttribute("loginUser", loginUser);

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
        HttpSession session = request.getSession();

        if (session != null) {
            log.info("User signed out = {}", session.getAttribute("loginUser"));
            session.invalidate();
        }

        return "redirect:/";
    }

}
