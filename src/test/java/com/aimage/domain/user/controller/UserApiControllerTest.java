package com.aimage.domain.user.controller;

import com.aimage.domain.user.entity.User;
import com.aimage.domain.user.service.UserService;
import com.aimage.web.user.UserApiController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static com.aimage.domain.user.dto.UserDto.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureRestDocs(outputDir = "build/generated-snippets/users")
@WebMvcTest(UserApiController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@WithMockUser
class UserApiControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Autowired
    ObjectMapper objectMapper;

    static User testUser;

    @BeforeAll
    static void setTestUser() {
        testUser = User.builder()
                .id(1L)
                .email("test@gmail.com")
                .username("tester")
                .password("testpass!")
                .build();
    }

    @Test
    @Order(1)
    @DisplayName("회원가입 API 테스트")
    void signup() throws Exception {
        // Given
        SignupRequest signupForm = new SignupRequest(
                testUser.getUsername(),
                testUser.getEmail(),
                testUser.getPassword(),
                testUser.getPassword()
        );

        String content = objectMapper.writeValueAsString(signupForm);

        given(userService.join(any(SignupRequest.class)))
                .willReturn(new UserResponse(testUser));

        // When & Then
        mockMvc.perform(post("/api/users")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                )
                .andDo(print())
                .andDo(document("signup",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("username").type(JsonFieldType.STRING).description("닉네임"),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                                fieldWithPath("confirmPassword").type(JsonFieldType.STRING).description("비밀번호 확인")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("사용자 id"),
                                fieldWithPath("username").type(JsonFieldType.STRING).description("닉네임"),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일")
                        ))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(testUser.getId()))
                .andExpect(jsonPath("username").value(testUser.getUsername()))
                .andExpect(jsonPath("email").value(testUser.getEmail()));

    }

    @Test
    @Order(2)
    @DisplayName("로그인 API 테스트")
    void login() throws Exception {
        // Given
        LoginRequest loginForm = new LoginRequest(
                testUser.getEmail(),
                testUser.getPassword());

        given(userService.login(any(LoginRequest.class)))
                .willReturn(new UserResponse(testUser));

        // When & Then
        mockMvc.perform(post("/api/users/login")
                        .content(objectMapper.writeValueAsString(loginForm))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                )
                .andDo(print())
                .andDo(document("login",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("사용자 id"),
                                fieldWithPath("username").type(JsonFieldType.STRING).description("닉네임"),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일")
                        ))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(testUser.getId()))
                .andExpect(jsonPath("username").value(testUser.getUsername()))
                .andExpect(jsonPath("email").value(testUser.getEmail()));
    }


    @Test
    @Order(3)
    @DisplayName("비밀번호 변경 전 이메일 인증 API 테스트")
    void findUserToResetPw() throws Exception {
        // Given
        PasswordInquiry pwInquiry = new PasswordInquiry(testUser.getEmail());
        given(userService.findUserToResetPw(any(PasswordInquiry.class)))
                .willReturn(new UserResponse(testUser));

        // When & Then
        mockMvc.perform(get("/api/users/pw-inquiry")
                        .content(objectMapper.writeValueAsString(pwInquiry))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                )
                .andDo(document("pw-inquiry",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("사용자 id"),
                                fieldWithPath("username").type(JsonFieldType.STRING).description("닉네임"),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일")
                        ))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(testUser.getId()))
                .andExpect(jsonPath("username").value(testUser.getUsername()))
                .andExpect(jsonPath("email").value(testUser.getEmail()));
    }


    @Test
    @Order(4)
    @DisplayName("비밀번호 변경 API 테스트")
    void resetPw() throws Exception {
        // Given
        PasswordUpdate passwordUpdate = new PasswordUpdate(
                "testpass1234",
                "testpass1234");

        given(userService.updatePassword(any(), any()))
                .willReturn(new UserResponse(testUser));

        String uri = String.format("/api/users/%d/new-pw", testUser.getId());

        // When & Then
        mockMvc.perform(put(uri)
                        .content(objectMapper.writeValueAsString(passwordUpdate))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                )
                .andDo(document("new-pw",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                                fieldWithPath("confirmPassword").type(JsonFieldType.STRING).description("비밀번호 확인")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("사용자 id"),
                                fieldWithPath("username").type(JsonFieldType.STRING).description("닉네임"),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일")
                        ))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(testUser.getId()))
                .andExpect(jsonPath("username").value(testUser.getUsername()))
                .andExpect(jsonPath("email").value(testUser.getEmail()));
    }

    @Test
    @Order(5)
    @DisplayName("닉네임 변경 API 테스트")
    void updateUsername() throws Exception {
        // Given
        UsernameUpdate usernameUpdate = new UsernameUpdate("newTester");
        testUser.updateUsername("newTester");
        String uri = String.format("/api/users/%d/new-username", testUser.getId());

        given(userService.updateUsername(any(), any()))
                .willReturn(new UserResponse(testUser));

        // When & Then
        mockMvc.perform(put(uri)
                        .content(objectMapper.writeValueAsString(usernameUpdate))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                )
                .andDo(document("new-username",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("username").type(JsonFieldType.STRING).description("새로운 닉네임")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("사용자 id"),
                                fieldWithPath("username").type(JsonFieldType.STRING).description("새로운 닉네임"),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일")
                        ))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(testUser.getId()))
                .andExpect(jsonPath("username").value(usernameUpdate.getUsername()))
                .andExpect(jsonPath("email").value(testUser.getEmail()));

    }

    @Test
    @Order(6)
    @DisplayName("계정 삭제 API 테스트")
    void deleteAccount() throws Exception {
        // Given
        String uri = String.format("/api/users/%d", testUser.getId());

        // When & Then
        mockMvc.perform(delete(uri)
                        .contentType(MediaType.TEXT_PLAIN)
                        .with(csrf())
                )
                .andDo(document("delete-account",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()))
                )
                .andExpect(status().isOk());
    }

}