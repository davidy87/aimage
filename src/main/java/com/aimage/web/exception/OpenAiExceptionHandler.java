package com.aimage.web.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class OpenAiExceptionHandler {

//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    @ExceptionHandler
//    public ErrorResult exHandler(Exception e) {
//        log.error("[exceptionHandler] ex", e);
//        return new ErrorResult("EX", "내부 오류");
//
//    }
}
