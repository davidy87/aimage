package com.aimage.domain.image.controller;

import com.aimage.domain.image.dto.ImageVO;
import com.aimage.domain.image.service.ImageService;

import com.aimage.domain.user.dto.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @GetMapping("/public-gallery")
    public String publicGallery(@SessionAttribute(required = false) UserVO loginUser,
                                Pageable pageable,
                                Model model) {

        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, 5, Sort.Direction.DESC, "id");
        Page<ImageVO> pagedImages = imageService.findPagedImages(pageable);

        model.addAttribute("pagedImages", new PagedImages(pagedImages));

        return "features/public-gallery";
    }

    @GetMapping("/public-gallery/details")
    public String imageDetails(@SessionAttribute(required = false) UserVO loginUser,
                               @RequestParam Long imageId,
                               Model model) {

        ImageVO image = imageService.findImageById(imageId);
        model.addAttribute("image", image);

        return "features/image-details";
    }
}
