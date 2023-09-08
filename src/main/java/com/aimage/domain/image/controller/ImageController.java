package com.aimage.domain.image.controller;

import com.aimage.domain.image.dto.ImageDto;
import com.aimage.domain.image.service.ImageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

        GeneratedImage imageResult = imageService.requestImageToOpenAI(imageRequest);
        model.addAttribute("imageResult", imageResult);

        log.info("Image generated: {}", imageResult);

        return "redirect:/result";
    }

    @GetMapping("/result")
    public String imageResult() {
        return "features/result";
    }

    @GetMapping("/public-gallery")
    public String publicGallery(Pageable pageable, Model model) {
        Page<ImageResponse> pagedImages = imageService.findPagedImages(pageable);
        model.addAttribute("pagedImages", new PagedImages(pagedImages));

        return "features/public-gallery";
    }

    @GetMapping("/public-gallery/details")
    public String imageDetails(@RequestParam Long imageId, Model model) {
        ImageResponse image = imageService.findImageById(imageId);
        model.addAttribute("image", image);

        return "features/image-details";
    }
}
