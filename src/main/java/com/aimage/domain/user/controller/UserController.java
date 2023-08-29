package com.aimage.domain.user.controller;

import com.aimage.domain.image.dto.ImageVO;
import com.aimage.domain.user.dto.UserDto;
import com.aimage.domain.user.entity.User;
import com.aimage.domain.user.service.UserService;
import com.aimage.domain.user.dto.UserVO;
import com.aimage.constant.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import static com.aimage.constant.SessionConst.LOGIN_USER;
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

    @GetMapping("/pw-inquiry")
    public String findPasswordForm() {
        return "user/pw-inquiry";
    }

    @GetMapping("/pw-inquiry/{id}/new-pw")
    public String updatePwForm(@PathVariable Long id, Model model) {
        model.addAttribute("userId", id);
        return "user/update-pw";
    }

    @GetMapping("/user-info")
    public String userInfo() {
        return "user/user-info";
    }

    @GetMapping("/my-gallery")
    public String myGallery(@AuthenticationPrincipal User loginUser,
                            Pageable pageable,
                            Model model) {

        Page<ImageVO> savedImages = userService.findSavedImages(loginUser.getId(), pageable);
        model.addAttribute("pagedImages", new PagedImages(savedImages));

        return "user/my-gallery";
    }

    @GetMapping("/my-gallery/details")
    public String showImageDetails(@AuthenticationPrincipal User loginUser,
                                   @RequestParam Long imageId,
                                   Model model) {

        ImageVO image = userService.findByOwnerIdAndImageId(loginUser.getId(), imageId);
        model.addAttribute("image", image);

        return "features/image-details";
    }
}
