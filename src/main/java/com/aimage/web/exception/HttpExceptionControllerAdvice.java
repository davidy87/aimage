package com.aimage.web.exception;

import com.aimage.domain.image.Image;
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
     * @param e OpenAiException: com.theokanning.openai.OpenAiHttpException 을 catch 할 때 던지는 커스텀 예외
     * @param model
     * @return redirect to image generating page (/generate)
     */
    @ExceptionHandler
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // INTERNAL_SERVER_ERROR 적용 시 redirect 작동 X
    public String openAiHttpExceptionHandler(OpenAiException e, Model model, RedirectAttributes redirectAttributes) {
        log.error("[openAiHttpExceptionHandler] {}", e.toString());
        model.addAttribute("image", new Image());
        redirectAttributes.addFlashAttribute("openAiError", true);
        return "redirect:/generate";
    }

}
