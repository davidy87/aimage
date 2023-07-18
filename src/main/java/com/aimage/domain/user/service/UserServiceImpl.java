package com.aimage.domain.user.service;

import com.aimage.domain.user.dto.SignupDTO;
import com.aimage.domain.user.entity.User;
import com.aimage.domain.user.repository.UserRepository;
import com.aimage.web.exception.SignupException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User join(SignupDTO signupDTO) {
        // 비밀번호 재입력 일치 확인
        if (!signupDTO.getPassword().equals(signupDTO.getConfirmPassword())) {
            return null;
        }

        User user = User.builder()
                .username(signupDTO.getUsername())
                .email(signupDTO.getEmail())
                .password(signupDTO.getPassword())
                .build();

        return userRepository.save(user);
    }

    @Override
    public User login(String loginIn, String password) {
        return userRepository.findByEmail(loginIn)
                .filter(m -> m.getPassword().equals(password))
                .orElse(null);
    }

    @Override
    public String findPassword(String email) {
        User userFound = userRepository.findByEmail(email).orElse(null);

        if (userFound == null) {
            return null;
        }

        return null;
    }

}
