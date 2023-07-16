package com.aimage;

import com.aimage.domain.user.User;
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
        User user = new User("tester", "test@gmail.com", "test!");
        userRepository.save(user);
    }

}
