package com.aimage.web.controller;

import com.aimage.domain.image.Image;
import com.aimage.domain.image.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BaseController {

    private final ImageRepository imageRepository;

    @GetMapping("/generate")
    public String generateForm(Model model) {
        model.addAttribute("image", new Image());
        return "features/generator";
    }

    @PostMapping("/result")
    public String generate(@ModelAttribute Image image) {
        log.info("Image generated: {}", image);
        imageRepository.save(image);
        return "features/result";
    }
}
