package com.aimage.domain.user.controller;

import com.aimage.domain.image.dto.ImageVO;
import com.aimage.domain.image.entity.Image;
import com.aimage.domain.user.service.UserService;
import com.aimage.domain.user.dto.UserVO;
import com.aimage.web.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
        model.addAttribute("userId", loginUser.id());
        return "user/userInfo";
    }

    @GetMapping("/myGallery")
    public String myGallery(@SessionAttribute(required = false) UserVO loginUser,
                            Pageable pageable,
                            Model model) {

        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, 5, Sort.Direction.DESC, "id");

        Page<ImageVO> savedImages = userService.findSavedImages(loginUser.id(), pageable)
                .map(image -> new ImageVO(image.getId(), image.getPrompt(), image.getUrl()));

        int number = savedImages.getNumber();
        int size = savedImages.getSize();
        int totalPage = savedImages.getTotalPages();

        int start = (int) Math.floor((double) number / size) * size + 1;
        int end = Math.min(start + size - 1, totalPage);

        model.addAttribute("startNumber", start);
        model.addAttribute("endNumber", end);
        model.addAttribute("savedImages", savedImages);

        return "user/myGallery";
    }
}
