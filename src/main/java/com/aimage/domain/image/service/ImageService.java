package com.aimage.domain.image.service;

import com.aimage.domain.image.dto.ImageVO;
import com.aimage.domain.image.entity.Image;
import com.aimage.domain.image.repository.ImageRepository;
import com.aimage.domain.user.entity.User;
import com.aimage.domain.user.repository.UserRepository;
import com.aimage.util.exception.AimageException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.aimage.constant.PageConst.PAGE_SIZE;
import static com.aimage.domain.image.dto.ImageDto.*;
import static com.aimage.util.exception.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ImageService {

    private final ImageRepository imageRepository;

    private final UserRepository userRepository;

    private final OpenAiClientService openAiClientService;

    public ImageVO save(Long userId, ImageResult imageResult) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new AimageException(IMAGE_SAVE_FAILED));

        Image image = Image.builder()
                .prompt(imageResult.getPrompt())
                .size(imageResult.getSize())
                .url(imageResult.getUrl())
                .build();

        image.setOwner(owner);
        imageRepository.save(image);

        return new ImageVO(image.getId(), image.getPrompt(), image.getUrl(), owner.getUsername());
    }

    public ImageResult requestImageToOpenAI(ImageRequest imageRequest) {
        String imageUrl = openAiClientService.requestImage(imageRequest);
        return new ImageResult(imageRequest.getPrompt(), imageRequest.getSize(), imageUrl);
    }

    public ImageVO findImageById(Long imageId) {
        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new AimageException(IMAGE_NOT_FOUND));

        return new ImageVO(image.getId(), image.getPrompt(), image.getUrl(), image.getOwner().getUsername());
    }

    public Page<ImageVO> findPagedImages(Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, PAGE_SIZE, Sort.Direction.DESC, "id");

        return imageRepository.findAll(pageable)
                .map(image -> new ImageVO(image.getId(), image.getPrompt(), image.getUrl(), image.getOwner().getUsername()));
    }

    public void delete(Long ownerId, Long imageId) {
        User owner = userRepository.findById(ownerId).orElseThrow(() ->
                new AimageException(IMAGE_DELETE_FAILED));

        Image imageToDelete = imageRepository.findById(imageId).orElseThrow(() ->
                new AimageException(IMAGE_ALREADY_NOT_EXIST));

        imageToDelete.setOwner(null);
        imageRepository.delete(imageToDelete);
    }
}
