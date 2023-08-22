package com.aimage.domain.user.repository;

import com.aimage.domain.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    User user;

    @BeforeEach
    void buildUser() {
        user = User.builder()
                .username("tester")
                .email("user1@gmail.com")
                .password("testpass!")
                .build();
    }

    @Test
    void findUser() {
        // Given
        User savedUser = userRepository.save(user);

        // When
        User byId = userRepository.findById(user.getId()).get();
        User byName = userRepository.findByUsername(user.getUsername()).get();
        User byEmail = userRepository.findByEmail(user.getEmail()).get();

        // Then
        checkUsersFound(savedUser, byId, byName, byEmail);
        assertThat(userRepository.findAll()).contains(savedUser);
    }

    @Test
    void updateUsername() {
        // Given
        User savedUser = userRepository.save(user);

        // When
        String newUsername = "newTester";
        savedUser.updateUsername(newUsername);

        // Then
        User updatedUser = userRepository.findById(savedUser.getId()).get();
        assertThat(updatedUser.getUsername()).isEqualTo(newUsername);
        assertThat(updatedUser.getEmail()).isEqualTo(savedUser.getEmail());
        assertThat(updatedUser.getPassword()).isEqualTo(savedUser.getPassword());
    }

    @Test
    void updatePassword() {
        // Given
        User savedUser = userRepository.save(user);

        // When
        String newPassword = "newpass1234";
        savedUser.updatePassword(newPassword);

        // Then
        User updatedUser = userRepository.findById(user.getId()).get();
        assertThat(updatedUser.getUsername()).isEqualTo(savedUser.getUsername());
        assertThat(updatedUser.getEmail()).isEqualTo(savedUser.getEmail());
        assertThat(updatedUser.getPassword()).isEqualTo(newPassword);
    }

    private void checkUsersFound(User savedUser, User... users) {
        for (User user : users) {
            assertThat(user).isEqualTo(savedUser);
        }
    }
}