package com.aimage.domain.image.controller;

import com.aimage.domain.image.dto.ImageVO;
import com.aimage.domain.image.service.ImageService;
import com.aimage.domain.user.dto.UserVO;

import com.aimage.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ImageVO saveImage(@AuthenticationPrincipal User loginUser,
                             @RequestBody ImageResult imageResult) {

        return imageService.save(loginUser.getId(), imageResult);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}")
    public String deleteImage(@AuthenticationPrincipal User loginUser,
                              @PathVariable Long id) {

        imageService.delete(loginUser.getId(), id);
        return "success";
    }

}
