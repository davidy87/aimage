package com.aimage.domain.user.repository;

import com.aimage.domain.user.entity.User;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

//@Repository
public class MemoryUserRepository implements UserRepository {

    private static Map<Long, User> repo = new ConcurrentHashMap<>();
    private static AtomicLong seq = new AtomicLong(0);

    @Override
    public User save(User user) {
//        user.setId(seq.incrementAndGet());
        repo.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(repo.get(id));
    }

    @Override
    public Optional<User> findByName(String username) {
        return repo.values().stream()
                .filter(user -> user.getUsername().equals(username))
                .findAny();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return repo.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findAny();
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(repo.values());
    }
}
