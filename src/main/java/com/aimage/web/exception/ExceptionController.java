package com.aimage.web.exception;

import com.aimage.domain.user.dto.UserDto;
import com.theokanning.openai.OpenAiHttpException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
@ControllerAdvice
public class ExceptionController {

    /**
     * @valid  유효성체크에 통과하지 못하면  MethodArgumentNotValidException 이 발생한다.
     */
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResult> methodValidException(MethodArgumentNotValidException e, HttpServletRequest request){
        log.warn("MethodArgumentNotValidException 발생!!! url:{}, trace:{}", request.getRequestURI(), e.getStackTrace());
        ErrorResult errorResponse = makeErrorResult(e.getBindingResult());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }


    @ResponseBody
    @ExceptionHandler
    public ResponseEntity<ErrorResult> aimageUserExceptionHandler(AimageUserException e) {
        log.info("Inside ControllerAdvice");
        return new ResponseEntity<>(new ErrorResult("ex", e.getMessage()), HttpStatus.BAD_REQUEST);
    }


    private ErrorResult makeErrorResult(BindingResult bindingResult) {
        String code = "";
        String message = "";

        //에러가 있다면
        if(bindingResult.hasErrors()) {
            FieldError passwordError = bindingResult.getFieldError("password");
            FieldError confirmPasswordError = bindingResult.getFieldError("confirmPassword");

            if (passwordError != null) {
                code = "pwError";
                message = passwordError.getDefaultMessage();
            } else if (confirmPasswordError != null) {
                code = "confirmPwError";
                message = confirmPasswordError.getDefaultMessage();
            }
        }

        return new ErrorResult(code, message);
    }
}
