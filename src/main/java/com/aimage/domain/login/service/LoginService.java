package com.aimage.domain.login.service;

import com.aimage.domain.user.User;
import org.springframework.stereotype.Service;

public interface LoginService {

    User login(String loginId, String password);
}
