package com.aimage.domain.image.controller;

import com.aimage.domain.user.dto.UserVO;
import com.aimage.domain.user.entity.User;
import com.aimage.domain.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import static com.aimage.domain.image.dto.ImageDto.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ImageApiControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired UserRepository userRepository;

    ObjectMapper om = new ObjectMapper();

    MockHttpSession session;

    @BeforeEach
    void createSession() {
        session = new MockHttpSession();
        User user = User.builder().build();
        userRepository.save(user);

        session.setAttribute("loginUser", new UserVO(user.getId(), user.getUsername(), user.getEmail()));
    }

    @Test
    @Order(1)
    void saveImage() throws Exception {
        ImageResult imageResult = new ImageResult("Hello", "256X256", "image.png");

        mockMvc.perform(post("/api/images")
                        .session(session)
                        .content(om.writeValueAsString(imageResult))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value("1"))
                .andExpect(jsonPath("prompt").value("Hello"))
                .andExpect(jsonPath("url").value("image.png"));
    }

    @Test
    @Order(2)
    void deleteImage() throws Exception {
        // 요청 성공
        mockMvc.perform(delete("/api/images/1")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().string("success"));

        // MySQL Delete은 해당 id가 없어도 성공으로 처리 (오류 X)
        mockMvc.perform(delete("/api/images/1")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().string("success"));
    }
}