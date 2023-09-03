package com.aimage.util.exception;

import lombok.Getter;

@Getter
public class AimageException extends RuntimeException {

    private String field;

    private String message;

    public AimageException(String message) {
        this.message = message;
    }

    public AimageException(String field, String message) {
        this.field = field;
        this.message = message;
    }

    public AimageException(ErrorCode errorCode) {
        this.field = errorCode.getField();
        this.message = errorCode.getMessage();
    }
}
