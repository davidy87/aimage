package com.aimage.domain.user.repository;

import com.aimage.domain.user.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User save(User user);

    Optional<User> findById(Long id);
    Optional<User> findByName(String username);
    Optional<User> findByEmail(String email);
    List<User> findAll();



    void updateUsername(Long id, String newUsername);
    void updatePassword(Long id, String newPassword);

    void delete(Long id);
}
