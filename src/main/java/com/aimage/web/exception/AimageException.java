package com.aimage.web.exception;

import lombok.Getter;

@Getter
public class AimageException extends RuntimeException {

    private String field;

    private String message;

    public AimageException(String message) {
        super(message);
    }

    public AimageException(String field, String message) {
        this.field = field;
        this.message = message;
    }

}
