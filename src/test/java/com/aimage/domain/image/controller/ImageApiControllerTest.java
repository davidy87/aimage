package com.aimage.domain.image.controller;

import com.aimage.domain.image.dto.ImageVO;
import com.aimage.domain.image.service.ImageService;
import com.aimage.domain.user.dto.UserVO;
import com.aimage.domain.user.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import static com.aimage.domain.image.dto.ImageDto.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
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

    static MockHttpSession session;


    @BeforeAll
    static void createSession() {
        session = new MockHttpSession();
        User user = User.builder().build();

        session.setAttribute("loginUser", new UserVO(user.getId(), user.getUsername(), user.getEmail()));
    }

    @Test
    @Order(1)
    void saveImage() throws Exception {
        // Given
        Long userId = ((UserVO) session.getAttribute("loginUser")).id();
        ImageResult imageResult = new ImageResult("This is a test image", "256X256", "image.png");
        ImageVO imageResponse = new ImageVO(1L, imageResult.getPrompt(), imageResult.getUrl());

        given(imageService.save(eq(userId), any(ImageResult.class)))
                .willReturn(imageResponse);

        // When & Then
        mockMvc.perform(post("/api/images")
                        .session(session)
                        .content(om.writeValueAsString(imageResult))
                        .contentType(MediaType.APPLICATION_JSON)
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
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("이미지 id"),
                                fieldWithPath("prompt").type(JsonFieldType.STRING).description("이미지 설명"),
                                fieldWithPath("url").type(JsonFieldType.STRING).description("이미지 URL")
                        ))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(imageResponse.id()))
                .andExpect(jsonPath("prompt").value(imageResponse.prompt()))
                .andExpect(jsonPath("url").value(imageResponse.url()));
    }

    @Test
    @Order(2)
    void deleteImage() throws Exception {
        Long imageId = 1L;

        // 요청 성공
        mockMvc.perform(delete("/api/images/" + imageId)
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(document("delete-image",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()))
                )
                .andExpect(status().isOk())
                .andExpect(content().string("success"));
    }
}