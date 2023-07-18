package com.aimage;

import com.aimage.domain.user.entity.User;
import com.aimage.domain.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TestUserInit {

    private final UserRepository userRepository;

    @PostConstruct
    public void init() {
        User user = User.builder()
                .username("tester")
                .email("test@gmail.com")
                .password("test!")
                .build();

        userRepository.save(user);
    }

}
