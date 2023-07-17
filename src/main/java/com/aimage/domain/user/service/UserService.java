package com.aimage.domain.user.service;

import com.aimage.domain.image.Image;
import com.aimage.domain.user.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;

public interface UserService {

    void join(User user);
}
