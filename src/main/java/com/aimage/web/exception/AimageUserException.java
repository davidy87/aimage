package com.aimage.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "error.signupFail")
public class AimageUserException extends RuntimeException {

    public AimageUserException(String message) {
        super(message);
    }
}
