package com.aimage.domain.user.login;

import com.aimage.domain.user.User;
import com.aimage.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;

    public User login(String loginId, String password) {
        return userRepository.findByEmail(loginId)
                .filter(m -> m.getPassword().equals(password))
                .orElse(null);
    }
}
