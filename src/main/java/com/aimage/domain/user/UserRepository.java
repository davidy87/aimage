package com.aimage.domain.user;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByName(String username);
    Optional<User> findByEmail(String email);
    List<User> findAll();

}
