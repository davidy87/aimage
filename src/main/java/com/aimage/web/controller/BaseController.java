package com.aimage.web.controller;

import com.aimage.domain.image.Image;
import com.aimage.domain.image.ImageRepository;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.image.CreateImageRequest;
import com.theokanning.openai.service.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BaseController {

    private final ImageRepository imageRepository;

    @Value("${openai-key}")
    private String OPENAI_KEY;

    @GetMapping("/generate")
    public String generateForm(Model model) {
        model.addAttribute("image", new Image());
        return "features/generator";
    }

    @PostMapping("/result")
    public String generate(@ModelAttribute Image image, Model model) {
        String imageUrl = openAiImageUrl(image);
        image.setUrl(imageUrl);
        imageRepository.save(image);

        log.info("Image generated: {}", image);

        model.addAttribute("imageUrl", imageUrl);
        return "features/result";
    }

    /**
     *
     * @param imageToRequest Image details to request
     * @return An Image URL generated by OpenAI model.
     *
     * Library used: openai-java by Theo Kanning
     */
    private String openAiImageUrl(Image imageToRequest) {
        OpenAiService service = new OpenAiService(OPENAI_KEY);
        CreateImageRequest createImageRequest = CreateImageRequest.builder()
                .prompt(imageToRequest.getPrompt())
                .n(1)
                .size(imageToRequest.getSize())
                .build();

        String imgUrl = service.createImage(createImageRequest)
                .getData()
                .get(0)
                .getUrl();

        return imgUrl;
    }
}
