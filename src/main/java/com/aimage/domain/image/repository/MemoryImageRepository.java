package com.aimage.domain.image.repository;

import com.aimage.domain.image.entity.Image;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

//@Repository
public class MemoryImageRepository implements ImageRepository {

    private Map<Long, Image> repo = new ConcurrentHashMap<>();
    AtomicLong seq = new AtomicLong(0);

    @Override
    public Image save(Image image) {
        image.setId(seq.incrementAndGet());
        repo.put(image.getId(), image);
        return image;
    }

    @Override
    public Optional<Image> findById(Long id) {
        return Optional.ofNullable(repo.get(id));
    }

    @Override
    public List<Image> findAll() {
        return new ArrayList<>(repo.values());
    }

    @Override
    public List<Image> findAllBySize(String size) {
        List<Image> result = repo.values().stream()
                .filter(image -> image.getSize().equals(size))
                .toList();

        return new ArrayList<>(result);
    }

    public void clearAll() {
        repo.clear();
    }
}
