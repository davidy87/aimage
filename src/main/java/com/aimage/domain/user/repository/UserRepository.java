package com.aimage.domain.user.repository;

import com.aimage.domain.user.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.username = :newUsername where m.id = :id")
    void updateUsername(Long id, String newUsername);

    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.password = :newPassword where m.id = :id")
    void updatePassword(Long id, String newPassword);

}
