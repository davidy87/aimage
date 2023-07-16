package com.aimage.domain.user.service;

import com.aimage.domain.image.Image;

public interface UserService {

    void join(Image image);
    Image findUser(Long imageId);
}
