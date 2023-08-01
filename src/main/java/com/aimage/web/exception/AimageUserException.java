package com.aimage.web.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Optional;

@Getter
public class AimageUserException extends RuntimeException {

    private String field;

    private String message;

    public AimageUserException(String message) {
        super(message);
    }

    public AimageUserException(String field, String message) {
        this.field = field;
        this.message = message;
    }

}
