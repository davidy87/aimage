package com.aimage;

import com.aimage.domain.user.entity.User;
import com.aimage.domain.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Profile("local")
@Component
@RequiredArgsConstructor
public class TestUserInit {

    private final UserRepository userRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        User user = User.builder()
                .username("tester")
                .email("test@gmail.com")
                .password("test!")
                .build();

        userRepository.save(user);
    }

}
