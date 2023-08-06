package com.aimage.domain.image.controller;

import com.aimage.domain.image.service.ImageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.aimage.domain.image.dto.ImageDto.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@SessionAttributes("imageResult")
public class ImageController {

    private final ImageService imageService;

    @GetMapping("/generator")
    public String generateForm(@ModelAttribute ImageRequest imageRequest) {
        return "features/generator";
    }

    @PostMapping("/generator")
    public String generate(@Validated @ModelAttribute ImageRequest imageRequest,
                           BindingResult bindingResult,
                           Model model) {

        if (bindingResult.hasErrors()) {
            return "features/generator";
        }

        ImageResult imageResult = imageService.requestImageToOpenAI(imageRequest);
        model.addAttribute("imageResult", imageResult);

        log.info("Image generated: {}", imageResult);

        return "redirect:/result";
    }

    @GetMapping("/result")
    public String imageResult() {
        return "features/result";
    }
}
