package com.aimage.domain.login.service;

import com.aimage.domain.user.User;
import com.aimage.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final UserRepository userRepository;

    public User login(String loginId, String password) {
        return userRepository.findByEmail(loginId)
                .filter(m -> m.getPassword().equals(password))
                .orElse(null);
    }
}
