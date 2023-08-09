package com.aimage.web.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class ExceptionController {

    /**
     *  유효성 체크에 통과하지 못하면 MethodArgumentNotValidException 이 발생한다.
     */
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity methodValidException(MethodArgumentNotValidException e, HttpServletRequest request){
        Map<String, List<ErrorResponse>> errorResponse = new HashMap<>();
        errorResponse.put("errors", makeErrorResult(e.getBindingResult()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     *
     * @param e AimageUserException 커스텀 예외
     */
    @ResponseBody
    @ExceptionHandler
    public ResponseEntity aimageUserExceptionHandler(AimageUserException e) {
        log.info("In aimageUserExceptionHandler");

        List<ErrorResponse> errorResponseList = new ArrayList<>();
        errorResponseList.add(new ErrorResponse(e.getField(), e.getMessage()));

        Map<String, List<ErrorResponse>> response = new HashMap<>();
        response.put("errors", errorResponseList);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }


    private List<ErrorResponse> makeErrorResult(BindingResult bindingResult) {
        List<ErrorResponse> errorResponseList = new ArrayList<>();

        for (FieldError fieldError: bindingResult.getFieldErrors()) {
            ErrorResponse errorResponse = new ErrorResponse(fieldError.getField(), fieldError.getDefaultMessage());
            errorResponseList.add(errorResponse);
        }

        return errorResponseList;
    }
}
