package com.aimage.web.user;

import com.aimage.domain.user.service.UserService;
import com.aimage.util.auth.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import static com.aimage.domain.image.dto.ImageDto.*;
import static com.aimage.util.exception.ErrorCode.*;

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
    public String loginForm(@RequestParam(required = false) boolean error,
                            @RequestParam(required = false) boolean oauth2Error,
                            Model model) {
        if (error) {
            model.addAttribute(LOGIN_ERROR.getField(), LOGIN_ERROR.getMessage());
        } else if (oauth2Error) {
            model.addAttribute(OAUTH2_LOGIN_ERROR.getField(), OAUTH2_LOGIN_ERROR.getMessage());
        }

        return "user/login-screen";
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
    public String myGallery(@AuthenticationPrincipal CustomUserDetails loginUser,
                            Pageable pageable,
                            Model model) {

        Page<ImageResponse> savedImages = userService.findSavedImages(loginUser.getId(), pageable);
        model.addAttribute("pagedImages", new PagedImages(savedImages));

        return "user/my-gallery";
    }

    @GetMapping("/my-gallery/details")
    public String showImageDetails(@AuthenticationPrincipal CustomUserDetails loginUser,
                                   @RequestParam Long imageId,
                                   Model model) {

        ImageResponse image = userService.findByOwnerIdAndImageId(loginUser.getId(), imageId);
        model.addAttribute("image", image);

        return "features/image-details";
    }
}
