package com.aimage.domain.image.controller;

import com.aimage.domain.image.dto.ImageDto;
import com.aimage.domain.image.service.ImageService;
import com.aimage.domain.user.entity.User;
import com.aimage.domain.image.entity.Image;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import static com.aimage.domain.image.dto.ImageDto.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureRestDocs(outputDir = "build/generated-snippets/images")
@WebMvcTest(ImageApiController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ImageApiControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ImageService imageService;

    @Autowired
    ObjectMapper om;

    static User testOwner;

    @BeforeAll
    static void createSession() {
        testOwner = User.builder()
                .id(1L)
                .username("tester")
                .email("test@gmail.com")
                .build();
    }

    @Test
    @Order(1)
    void saveImage() throws Exception {
        // Given
        GeneratedImage imageResult = new GeneratedImage("This is a test image", "256X256", "image.png");
        Image image = Image.builder()
                .id(1L)
                .prompt(imageResult.getPrompt())
                .size(imageResult.getSize())
                .url(imageResult.getUrl())
                .build();

        image.setOwner(testOwner);

        given(imageService.save(eq(testOwner.getId()), any(GeneratedImage.class)))
                .willReturn(new ImageResponse(image));

        // When & Then
        mockMvc.perform(post("/api/images")
                        .with(SecurityMockMvcRequestPostProcessors.user(testOwner))
                        .content(om.writeValueAsString(imageResult))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                )
                .andDo(document("new-image",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("prompt").type(JsonFieldType.STRING).description("이미지 설명"),
                                fieldWithPath("size").type(JsonFieldType.STRING).description("이메일"),
                                fieldWithPath("url").type(JsonFieldType.STRING).description("이미지 URL")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("이미지 ID"),
                                fieldWithPath("prompt").type(JsonFieldType.STRING).description("이미지 설명"),
                                fieldWithPath("url").type(JsonFieldType.STRING).description("이미지 URL"),
                                fieldWithPath("owner").type(JsonFieldType.STRING).description("이미지 소유자 닉네임")
                        ))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("prompt").value(image.getPrompt()))
                .andExpect(jsonPath("url").value(image.getUrl()))
                .andExpect(jsonPath("owner").value(testOwner.getUsername()));
    }

    @Test
    @Order(2)
    void deleteImage() throws Exception {
        long imageId = 1L;

        // 요청 성공
        mockMvc.perform(delete("/api/images/" + imageId)
                        .with(SecurityMockMvcRequestPostProcessors.user(testOwner))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                )
                .andDo(document("delete-image",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()))
                )
                .andExpect(status().isOk())
                .andExpect(content().string("success"));
    }
}