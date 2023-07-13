package com.aimage.web.user;

import com.aimage.domain.user.User;
import com.aimage.domain.user.UserRepository;
import com.aimage.domain.user.login.LoginService;
import com.aimage.web.user.login.LoginForm;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final LoginService loginService;

    @GetMapping("/signup")
    public String signupForm(@ModelAttribute User user) {
        return "login/signup-screen";
    }

    @PostMapping("/signup")
    public String signupModal(@Validated @ModelAttribute User user,
                              BindingResult bindingResult,
                              HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            return "login/signup-screen";
        }

        String confirmPwd = request.getParameter("confirmPassword");

        if (!confirmPwd.equals(user.getPassword())) {
            log.info("Sign up failed: {}", user);
            bindingResult.reject("signupFail", "Your confirmation password did not match.");
            return "login/signup-screen";
        }

        log.info("Sign up user = {}", user);
        userRepository.save(user);
        log.info("User list = {}", userRepository.findAll());

        return "redirect:/";
    }

    @GetMapping("/login")
    public String loginForm(@ModelAttribute LoginForm loginForm) {
        return "login/login-screen";
    }

    @PostMapping("/login")
    public String login(@Validated @ModelAttribute LoginForm loginForm, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return "login/login-screen";
        }

        User loginUser = loginService.login(loginForm.getEmail(), loginForm.getPassword());

        if (loginUser == null) {
            log.info("Login failed: {}", loginForm);
            bindingResult.reject("loginFail", "Your email or password is incorrect.");
            return "login/login-screen";
        }

        HttpSession session = request.getSession();
        session.setAttribute("loginUser", loginUser);

        log.info("Login user = {}", loginUser);
        return "redirect:/";
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
