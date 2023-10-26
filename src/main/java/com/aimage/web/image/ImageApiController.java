package com.aimage.web.image;

import com.aimage.domain.image.service.ImageService;

import com.aimage.util.config.auth.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.aimage.domain.image.dto.ImageDto.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/images")
public class ImageApiController {

    private final ImageService imageService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ImageResponse saveImage(@AuthenticationPrincipal CustomUserDetails loginUser,
                                   @RequestBody GeneratedImage imageResult) {

        log.info("Image result = {}", imageResult);

        return imageService.save(loginUser.getId(), imageResult);
    }

    @DeleteMapping("/{id}")
    public String deleteImage(@PathVariable Long id) {

        imageService.delete(id);
        return "success";
    }

}
