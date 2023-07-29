package com.aimage.domain.image.repository;

import com.aimage.domain.image.entity.Image;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ImageMapper {

    // Create
    void save(Image image);

    // Read
    Optional<Image> findById(Long id);
    List<Image> findAll();
    List<Image> findAllBySize(String size);
    List<Image> findAllByUserId(Long userId);

    // Delete
    void delete(Long id);
}
