package com.aimage.domain.image.controller;

import com.aimage.domain.image.dto.ImageDto;
import com.aimage.domain.image.service.ImageServiceImpl;
import com.aimage.domain.user.entity.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ImageController {

    private final ImageServiceImpl imageService;

    @GetMapping("/generate")
    public String generateForm(@SessionAttribute(required = false) User loginUser,
                               @ModelAttribute ImageDto.ImageRequest imageRequest) {

        return "features/generator";
    }

    @PostMapping("/generate")
    public String generate(@SessionAttribute(required = false) User loginUser,
                           @Validated @ModelAttribute ImageDto.ImageRequest imageRequest,
                           BindingResult bindingResult,
                           Model model) {

        if (bindingResult.hasErrors()) {
            return "features/generator";
        }

        String imageURL = imageService.requestImageToOpenAI(imageRequest);
        imageService.save(loginUser.getId(), imageRequest, imageURL);
        model.addAttribute("imageURL", imageURL);

        log.info("Image generated: {}", imageRequest);

        return "features/result";
    }

}
