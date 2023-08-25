package com.aimage.domain.user.controller;

import com.aimage.domain.image.dto.ImageVO;
import com.aimage.domain.user.service.UserService;
import com.aimage.domain.user.dto.UserVO;
import com.aimage.constant.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import static com.aimage.domain.image.dto.ImageDto.*;

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
        return "user/userInfo";
    }

    @GetMapping("/myGallery")
    public String myGallery(@SessionAttribute(required = false) UserVO loginUser,
                            Pageable pageable,
                            Model model) {

        Page<ImageVO> savedImages = userService.findSavedImages(loginUser.id(), pageable);
        model.addAttribute("pagedImages", new PagedImages(savedImages));

        return "user/myGallery";
    }

    @GetMapping("/myGallery/details")
    public String showImageDetails(@SessionAttribute(required = false) UserVO loginUser,
                                 @RequestParam Long imageId,
                                 Model model) {

        ImageVO image = userService.findByOwnerIdAndImageId(loginUser.id(), imageId);
        model.addAttribute("image", image);

        return "features/image-details";
    }
}
