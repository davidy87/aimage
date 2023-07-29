package com.aimage.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Optional;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "error.signupFail")
public class AimageUserException extends RuntimeException {

    private BindingResult bindingResult;

    public AimageUserException(String message) {
        super(message);
    }
}
