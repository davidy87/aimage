package com.aimage.domain.user.service;

import com.aimage.domain.user.dto.SignupDTO;
import com.aimage.domain.user.entity.User;

public interface UserService {

    User join(SignupDTO signupDTO);
    User login(String loginIn, String password);
    String findPassword(String email);
}
