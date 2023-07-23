package com.aimage.web.exception;

import com.theokanning.openai.OpenAiHttpException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@ControllerAdvice
public class HttpExceptionControllerAdvice {

    /**
     * OpenAI API 요청 시 생기는 예외 처리
     * @param e com.theokanning.openai.OpenAiHttpException
     * @param model
     * @return 이미지 생성 페이지로 리다이렉트 (/generate)
     */
    @ExceptionHandler
    public String openAiHttpExceptionHandler(OpenAiHttpException e, Model model, RedirectAttributes redirectAttributes) {
        log.error("[openAiHttpExceptionHandler] {}", e.toString());
        redirectAttributes.addFlashAttribute("openAiError", true);
        return "redirect:/generate";
    }

}
