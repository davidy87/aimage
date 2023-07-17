package com.aimage.domain.user.controller;

import com.aimage.domain.user.User;
import com.aimage.domain.user.repository.UserRepository;
import com.aimage.domain.login.service.LoginServiceImpl;
import com.aimage.domain.login.LoginForm;
import com.aimage.domain.user.service.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;
    private final LoginServiceImpl loginService;

    @GetMapping("/signup")
    public String signupForm(@ModelAttribute User user) {
        return "login/signup-screen";
    }

    @PostMapping("/signup")
    public String signupModal(@Validated @ModelAttribute User user,
                              BindingResult bindingResult,
                              HttpServletRequest request) {

        if (bindingResult.hasErrors() || !signupPasswordConfirmed(user, bindingResult, request)) {
            return "login/signup-screen";
        }

        log.info("Sign up user = {}", user);
        userService.join(user);

        return "redirect:/";
    }

    /**
     * 회원가입 비밀번호 재입력 확인
     */
    private static Boolean signupPasswordConfirmed(User user,
                                                   BindingResult bindingResult,
                                                   HttpServletRequest request) {

        String confirmPwd = request.getParameter("confirmPassword");

        if (!confirmPwd.equals(user.getPassword())) {
            log.info("Sign up failed: {}", user);
            bindingResult.reject("signupFail", "Your confirmation password did not match.");
            return false;
        }

        return true;
    }

    @GetMapping("/login")
    public String loginForm(@ModelAttribute LoginForm loginForm) {
        return "login/login-screen";
    }

    @PostMapping("/login")
    public String login(@Validated @ModelAttribute LoginForm loginForm,
                        BindingResult bindingResult,
                        HttpServletRequest request) {

        User loginUser = loginService.login(loginForm.getEmail(), loginForm.getPassword());

        if (bindingResult.hasErrors() || !isValidLogin(loginUser, loginForm, bindingResult)) {
            return "login/login-screen";
        }

        HttpSession session = request.getSession();
        session.setAttribute("loginUser", loginUser);

        log.info("Login user = {}", loginUser);
        return "redirect:/";
    }


    /**
     * 로그인 시 이메일 & 비밀번호 확인
     */
    private static Boolean isValidLogin(User loginUser, LoginForm loginForm, BindingResult bindingResult) {
        if (loginUser == null) {
            log.info("Login failed: {}", loginForm);
            bindingResult.reject("loginFail", "Your email or password is incorrect.");
            return false;
        }

        return true;
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
