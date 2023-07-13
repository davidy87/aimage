package com.aimage.web;

import com.aimage.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
@RequiredArgsConstructor
public class HomeController {

    @GetMapping("/")
    public String home(@SessionAttribute(name = "loginUser", required = false) User loginUser, Model model) {
        if (loginUser != null) {
            model.addAttribute("user", loginUser);
        }

        return "home";
    }
}
