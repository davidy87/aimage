package com.aimage.domain.user.repository;

import com.aimage.AimageApplication;
import com.aimage.domain.user.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
class DbUserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    void findUser() {
        User user = User.builder()
                .username("user1")
                .email("user1@gmail.com")
                .password("user1")
                .build();

        User savedUser = userRepository.save(user);

        User byId = userRepository.findById(user.getId()).get();
        User byName = userRepository.findByName(user.getUsername()).get();
        User byEmail = userRepository.findByEmail(user.getEmail()).get();

        findUserBy(savedUser, byId, byName, byEmail);
        assertThat(userRepository.findAll()).contains(savedUser);
    }

    @Test
    void updatePassword() {
        User user = User.builder()
                .username("user1")
                .email("user1@gmail.com")
                .password("user1")
                .build();

        User savedUser = userRepository.save(user);
        userRepository.updatePassword(savedUser.getId(), "newpass1234");
        User userWithNewPw = userRepository.findById(user.getId()).get();

        assertThat(userWithNewPw.getPassword()).isEqualTo("newpass1234");
    }

    private void findUserBy(User savedUser, User... users) {
        for (User user : users) {
            assertThat(user).isEqualTo(savedUser);
        }
    }
}