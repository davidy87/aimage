package com.aimage.domain.image.controller;

import com.aimage.domain.image.dto.ImageVO;
import com.aimage.domain.image.service.ImageService;
import com.aimage.domain.user.dto.UserVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.aimage.domain.image.dto.ImageDto.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/images")
public class ImageApiController {

    private final ImageService imageService;

    @PostMapping
    public ResponseEntity<ImageVO> saveImage(@SessionAttribute UserVO loginUser,
                                             @RequestBody ImageResult imageResult) {

        ImageVO savedImage = imageService.save(loginUser.id(), imageResult);
        return ResponseEntity.status(HttpStatus.OK).body(savedImage);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteImage(@SessionAttribute UserVO loginUser,
                                      @PathVariable Long id) {

        imageService.delete(loginUser.id(), id);
        return ResponseEntity.status(HttpStatus.OK).body("success");
    }

}
