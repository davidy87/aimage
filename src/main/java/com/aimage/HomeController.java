package com.aimage;

import com.aimage.domain.user.dto.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
@RequiredArgsConstructor
public class HomeController {

    @GetMapping("/")
    public String home(@SessionAttribute(name = "loginUser", required = false) UserVO loginUser, Model model) {
        if (loginUser != null) {
            model.addAttribute("user", loginUser);
        }

        return "home";
    }
}
