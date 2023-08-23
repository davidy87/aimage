package com.aimage.domain.image.service;

import com.aimage.domain.image.dto.ImageVO;
import com.aimage.domain.image.entity.Image;
import com.aimage.domain.image.repository.ImageRepository;
import com.aimage.domain.user.entity.User;
import com.aimage.domain.user.repository.UserRepository;
import com.aimage.web.exception.AimageException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.server.ResponseStatusException;
import retrofit2.HttpException;

import java.util.List;
import java.util.Optional;

import static com.aimage.domain.image.dto.ImageDto.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ImageService {

    private final ImageRepository imageRepository;

    private final UserRepository userRepository;

    private final OpenAiClientService openAiClientService;

    public ImageVO save(Long userId, ImageResult imageResult) {
        User owner = userRepository.findById(userId).orElseThrow(() ->
                new AimageException("이미지 저장에 실패했습니다."));

        Image image = Image.builder()
                .prompt(imageResult.getPrompt())
                .size(imageResult.getSize())
                .url(imageResult.getUrl())
                .build();

        image.setOwner(owner);
        imageRepository.save(image);

        return new ImageVO(image.getId(), image.getPrompt(), image.getUrl(), image.getOwner().getUsername());
    }

    public ImageResult requestImageToOpenAI(ImageRequest imageRequest) {
        String imageUrl = openAiClientService.requestImage(imageRequest);
        return new ImageResult(imageRequest.getPrompt(), imageRequest.getSize(), imageUrl);
    }

    public ImageVO findImageById(Long imageId) {
        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new AimageException("이미지를 찾을 수 없습니다."));

        return new ImageVO(image.getId(), image.getPrompt(), image.getUrl(), image.getOwner().getUsername());
    }

    public Page<ImageVO> findPagedImages(Pageable pageable) {
        return imageRepository.findAll(pageable)
                .map(image -> new ImageVO(image.getId(), image.getPrompt(), image.getUrl(), image.getOwner().getUsername()));
    }

    public void delete(Long ownerId, Long imageId) {
        User owner = userRepository.findById(ownerId).orElseThrow(() ->
                new AimageException("이미지를 삭제할 수 없습니다."));

        Image imageToDelete = imageRepository.findById(imageId).orElseThrow(() ->
                new AimageException("이미 존재하지 않는 이미지입니다."));

        imageToDelete.setOwner(null);
        imageRepository.delete(imageToDelete);
    }
}
