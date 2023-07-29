package com.aimage.domain.user.repository;

import com.aimage.domain.user.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface UserMapper {

    // Create
    void save(User user);

    // Read
    Optional<User> findById(Long id);
    Optional<User> findByName(String username);
    Optional<User> findByEmail(String email);
    List<User> findAll();

    // Update
    void updateUsername(Long id, String newUsername);
    void updatePassword(Long id, String newPassword);

    // Delete
    void delete(Long id);

}
