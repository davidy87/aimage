package com.aimage.domain.image.repository;

import com.aimage.domain.image.Image;

import java.util.List;
import java.util.Optional;

public interface ImageRepository {

    Image save(Image image);
    Optional<Image> findById(Long id);
    List<Image> findAll();
    List<Image> findAllBySize(String size);

}