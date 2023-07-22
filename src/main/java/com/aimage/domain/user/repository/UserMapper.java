package com.aimage.domain.user.repository;

import com.aimage.domain.user.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface UserMapper {

    void save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByName(@Param("username") String username);
    Optional<User> findByEmail(@Param("email") String email);
    List<User> findAll();
}
