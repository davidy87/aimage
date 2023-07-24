package com.aimage.domain.user.service;

import com.aimage.domain.user.dto.UserDto;
import com.aimage.domain.user.entity.User;
import com.aimage.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User join(UserDto.Signup signupDto) {
        // 비밀번호 재입력 일치 확인
        if (!signupDto.getPassword().equals(signupDto.getConfirmPassword())) {
            return null;
        }

        User user = User.builder()
                .username(signupDto.getUsername())
                .email(signupDto.getEmail())
                .password(signupDto.getPassword())
                .build();

        return userRepository.save(user);
    }

    @Override
    public User login(String email, String password) {
        return userRepository.findByEmail(email)
                .filter(m -> m.getPassword().equals(password))
                .orElse(null);
    }

    @Override
    public User findUserToResetPw(String email) {
        User userFound = userRepository.findByEmail(email).orElse(null);
        log.info("User found = {}", userFound);
        return userFound;
    }

    @Override
    public boolean updatePassword(User userToResetPw, UserDto.UpdatePassword updatePassword) {
        String password = updatePassword.getPassword();
        String confirmPassword = updatePassword.getConfirmPassword();

        if (password.equals(confirmPassword)) {
            userRepository.updatePassword(userToResetPw.getId(), password);
            return true;
        }

        return false;
    }

}
