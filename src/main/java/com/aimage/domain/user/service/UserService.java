package com.aimage.domain.user.service;

import com.aimage.domain.user.dto.UserDto;
import com.aimage.domain.user.entity.User;

public interface UserService {

    User join(UserDto.Signup signupDto);

    User login(String loginIn, String password);

    User findUserToResetPw(String email);

    boolean updatePassword(User userToResetPw, UserDto.UpdatePassword updatePassword);
}
