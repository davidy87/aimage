package com.aimage.domain.user.service;

import com.aimage.domain.image.Image;
import com.aimage.domain.user.User;
import com.aimage.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public void join(User user) {
        userRepository.save(user);
    }

}
