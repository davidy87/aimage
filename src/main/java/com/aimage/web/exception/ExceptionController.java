package com.aimage.web.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@ControllerAdvice
public class ExceptionController {

    /**
     *  유효성 체크에 통과하지 못하면 MethodArgumentNotValidException 이 발생한다.
     */
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity methodValidException(MethodArgumentNotValidException e, HttpServletRequest request){
        List<ErrorResponse> errorResponseList = makeErrorResult(e.getBindingResult());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseList);
    }

    /**
     *
     * @param e AimageUserException 커스텀 예외
     */
    @ResponseBody
    @ExceptionHandler
    public ResponseEntity aimageUserExceptionHandler(AimageUserException e) {
        List<ErrorResponse> errorResponseList = new ArrayList<>();
        errorResponseList.add(new ErrorResponse(e.getField(), e.getMessage()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseList);
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
