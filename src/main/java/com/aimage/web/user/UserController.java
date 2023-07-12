package com.aimage.web.user;

import com.aimage.domain.user.User;
import com.aimage.domain.user.UserRepository;
import com.aimage.domain.user.login.LoginService;
import com.aimage.web.user.login.LoginForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/signup")
    public String signupForm(@ModelAttribute User user) {
        return "login/signup-screen";
    }

    @PostMapping("/signup")
    public String signupModal(@ModelAttribute User user) {
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
    public String login(@ModelAttribute LoginForm loginForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        User loginUser = loginService.login(loginForm.getEmail(), loginForm.getPassword());

        if (loginUser == null) {
            log.info("Login failed: {}", loginForm);
            bindingResult.reject("loginFail", "Your email or password is incorrect.");
            return "login/login-screen";
        }

        redirectAttributes.addFlashAttribute("user", loginUser);
        log.info("Login user = {}", loginUser);
        return "redirect:/";
    }
}
