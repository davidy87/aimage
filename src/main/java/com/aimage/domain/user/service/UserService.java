package com.aimage.domain.user.service;

import com.aimage.domain.user.dto.UserDto;
import com.aimage.domain.user.entity.User;

public interface UserService {

    User join(UserDto.Signup signupDto);

    User login(String loginIn, String password);

    User findUserToResetPw(String email);

    boolean updateUsername(User userToUpdate, UserDto.UpdateUsername updateUsername);

    boolean updatePassword(User userToUpdate, UserDto.UpdatePassword updatePassword);
}
