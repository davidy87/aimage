package com.aimage.domain.user.controller;

import com.aimage.domain.user.dto.UserDto;
import com.aimage.domain.user.dto.UserVO;
import com.aimage.domain.user.repository.UserRepository;
import com.aimage.domain.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureRestDocs(outputDir = "build/generated-snippets/users")
@WebMvcTest(UserApiController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserApiControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;
//
    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @Order(1)
    @DisplayName("회원가입 API 테스트")
    void signup() throws Exception {
        // Given
        UserDto.Signup signupForm = new UserDto.Signup(
                "tester",
                "test@gmail.com",
                "testpass!",
                "testpass!"
        );

        String content = objectMapper.writeValueAsString(signupForm);

        given(userService.join(any()))
                .willReturn(new UserVO(1L, "tester", "test@gmail.com"));

        // When & Then
        // 요청 성공
        mockMvc.perform(post("/api/users")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andDo(document("signup"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value("1"))
                .andExpect(jsonPath("username").value("tester"))
                .andExpect(jsonPath("email").value("test@gmail.com"));

        // 요청 실패
        UserDto.Signup wrongForm = new UserDto.Signup(
                "good",
                "test@gmail.com",
                "",
                ""
        );

        String wrongContent = objectMapper.writeValueAsString(wrongForm);

        mockMvc.perform(post("/api/users")
                        .content(wrongContent)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andDo(document("signup-error"))
                .andExpect(status().is4xxClientError());

    }

    @Test
    @Order(2)
    @DisplayName("로그인 API 테스트")
    void login() throws Exception {
        UserDto.Login loginForm = new UserDto.Login(
                "test@gmail.com",
                "testpass!");

        // 요청 성공
        mockMvc.perform(post("/api/users/login")
                        .content(objectMapper.writeValueAsString(loginForm))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value("1"))
                .andExpect(jsonPath("username").value("tester"))
                .andExpect(jsonPath("email").value("test@gmail.com"));


        UserDto.Login loginFormError = new UserDto.Login(
                "test@gmail.com",
                "passTest");

        // 요청 실패
        mockMvc.perform(post("/api/users/login")
                        .content(objectMapper.writeValueAsString(loginFormError))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is4xxClientError());
    }


    @Test
    @Order(3)
    @DisplayName("비밀번호 변경 전 이메일 인증 API 테스트")
    void findUserToResetPw() throws Exception {
        UserDto.PwInquiry pwInquiry = new UserDto.PwInquiry("test@gmail.com");

        // 요청 성공
        mockMvc.perform(get("/api/users/pw-inquiry")
                        .content(objectMapper.writeValueAsString(pwInquiry))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value("1"))
                .andExpect(jsonPath("username").value("tester"))
                .andExpect(jsonPath("email").value("test@gmail.com"));


        UserDto.PwInquiry pwInquiryError = new UserDto.PwInquiry("test@naver.com");

        // 요청 실패
        mockMvc.perform(post("/api/users/pw-inquiry")
                        .content(objectMapper.writeValueAsString(pwInquiryError))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is4xxClientError());
    }


    @Test
    @Order(4)
    @DisplayName("비밀번호 변경 API 테스트")
    void resetPw() throws Exception {
        UserDto.UpdatePassword updatePassword = new UserDto.UpdatePassword(
                "testpass1234",
                "testpass1234");

        String content = objectMapper.writeValueAsString(updatePassword);

        mockMvc.perform(put("/api/users/1/new-pw")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value("1"))
                .andExpect(jsonPath("username").value("tester"))
                .andExpect(jsonPath("email").value("test@gmail.com"));


        UserDto.UpdatePassword updatePasswordError = new UserDto.UpdatePassword(
                "testpass1234",
                "testpass4321");

        // 요청 실패
        mockMvc.perform(post("/api/users/1/new-pw")
                        .content(objectMapper.writeValueAsString(updatePasswordError))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is4xxClientError());
    }

    @Test
    @Order(5)
    @DisplayName("닉네임 변경 API 테스트")
    void updateUsername() throws Exception {
        UserDto.UpdateUsername updateUsername = new UserDto.UpdateUsername("newTester");

        // 요청 성공
        mockMvc.perform(put("/api/users/1/new-username")
                        .content(objectMapper.writeValueAsString(updateUsername))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value("1"))
                .andExpect(jsonPath("username").value("newTester"))
                .andExpect(jsonPath("email").value("test@gmail.com"));


        updateUsername = new UserDto.UpdateUsername("newTester");

        // 요청 실패
        mockMvc.perform(put("/api/users/1/new-username")
                        .content(objectMapper.writeValueAsString(updateUsername))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is4xxClientError());
    }

    @Test
    @Order(6)
    @DisplayName("계정 삭제 API 테스트")
    void deleteAccount() throws Exception {

        // 요청 성공
        mockMvc.perform(delete("/api/users/1")
                        .contentType(MediaType.TEXT_PLAIN)
                )
                .andExpect(status().isOk());

        // 요청 실패
        mockMvc.perform(delete("/api/users/2")
                        .contentType(MediaType.TEXT_PLAIN)
                )
                .andExpect(status().is4xxClientError());
    }

}