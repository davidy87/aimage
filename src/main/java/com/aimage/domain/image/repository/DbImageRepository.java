package com.aimage.domain.image.repository;

import com.aimage.domain.image.entity.Image;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class DbImageRepository implements ImageRepository {

    private final ImageMapper imageMapper;

    @Override
    public Image save(Image image) {
        log.info("image saved = {}", image);
        imageMapper.save(image);
        return image;
    }

    @Override
    public Optional<Image> findById(Long id) {
        return imageMapper.findById(id);
    }

    @Override
    public List<Image> findAll() {
        return imageMapper.findAll();
    }

    @Override
    public List<Image> findAllBySize(String size) {
        return imageMapper.findAllBySize(size);
    }
}
