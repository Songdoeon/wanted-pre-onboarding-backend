package com.wanted.onboarding.controllder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanted.onboarding.dto.UserRequestDTO;
import com.wanted.onboarding.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@ExtendWith(SpringExtension.class)
@WithMockUser
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();
    @MockBean
    private UserService userService;


    @Test
    @DisplayName("회원가입")
    void signUp() throws Exception {
        UserRequestDTO userRequestDTO = new UserRequestDTO().builder()
                .username("song2124@wanted.com")
                .password("12341234")
                .build();

        given(userService.signUp(userRequestDTO)).willReturn(userRequestDTO.getUsername());

        mockMvc.perform(post("/sign-up")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("@가 없는 아이디")
    void usernameBindingError() throws Exception {
        UserRequestDTO userRequestDTO = new UserRequestDTO().builder()
                .username("song2124")
                .password("12341234")
                .build();

        given(userService.signUp(userRequestDTO)).willReturn(userRequestDTO.getUsername());

        mockMvc.perform(post("/sign-up")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("아이디는 이메일 형식이어야합니다."));
    }

    @Test
    @DisplayName("비밀번호가 8자 미만인 경우")
    void passwordBindingError() throws Exception {
        UserRequestDTO userRequestDTO = new UserRequestDTO().builder()
                .username("song2124@wanted.com")
                .password("1234")
                .build();

        given(userService.signUp(userRequestDTO)).willReturn(userRequestDTO.getUsername());

        mockMvc.perform(post("/sign-up")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("비밀번호는 8자 이상이어야합니다."));
    }
}