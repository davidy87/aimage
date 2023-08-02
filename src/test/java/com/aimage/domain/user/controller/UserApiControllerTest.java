package com.aimage.domain.user.controller;

import com.aimage.domain.user.dto.UserDto;
import com.aimage.domain.user.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserApiControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;
    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("회원가입 API 테스트")
    void signup() throws Exception {
        UserDto.Signup signupForm = new UserDto.Signup(
                "tester",
                "test@gmail.com",
                "testpass!",
                "testpass!"
        );

        String content = objectMapper.writeValueAsString(signupForm);

        // 요청 성공
        mockMvc.perform(post("/api/user/signup")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("ok"));

        // 요청 실패
        UserDto.Signup wrongForm = new UserDto.Signup(
                "good",
                "test@gmail.com",
                "",
                ""
        );

        String wrongContent = objectMapper.writeValueAsString(wrongForm);

        mockMvc.perform(post("/api/user")
                        .content(wrongContent)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is4xxClientError());

    }

    @Test
    void login() throws Exception {
        UserDto.Login loginForm = new UserDto.Login(
                "test@gmail.com",
                "testpass!");

        String content = objectMapper.writeValueAsString(loginForm);

        // 요청 성공
        mockMvc.perform(post("/api/user/login")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value("1"))
                .andExpect(jsonPath("username").value("tester"));
    }
}