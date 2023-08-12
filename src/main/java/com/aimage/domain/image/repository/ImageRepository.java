package com.aimage.domain.image.repository;

import com.aimage.domain.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    List<Image> findAllBySize(String size);

    List<Image> findAllByOwnerId(Long ownerId);

}
