package com.aimage.domain.user.controller;

import com.aimage.domain.image.entity.Image;
import com.aimage.domain.user.service.UserService;
import com.aimage.domain.user.dto.UserVO;
import com.aimage.web.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/signup")
    public String signupForm() {
        return "user/signup-screen";
    }

    @GetMapping("/login")
    public String loginForm() {
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
    public String findPasswordForm() {
        return "user/pwInquiry";
    }

    @GetMapping("/pwInquiry/{id}/newPw")
    public String updatePwForm(@PathVariable Long id, Model model) {
        model.addAttribute("userId", id);
        return "user/updatePw";
    }

    @GetMapping("/userInfo")
    public String userInfo(@SessionAttribute(required = false) UserVO loginUser, Model model) {
        model.addAttribute("userId", loginUser.getId());
        return "user/userInfo";
    }

    @GetMapping("/myGallery")
    public String myGallery(@SessionAttribute(required = false) UserVO loginUser, Model model) {
        List<Image> savedImages = userService.findSavedImages(loginUser.getId());
        model.addAttribute("savedImages", savedImages);

        return "user/myGallery";
    }
}
