package com.wanted.onboarding.controllder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanted.onboarding.dto.ArticleRequestDTO;
import com.wanted.onboarding.dto.ArticleResponseDTO;
import com.wanted.onboarding.service.ArticleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ArticleController.class)
@ExtendWith(SpringExtension.class)
@WithMockUser
class ArticleControllerTest {
    @Autowired
    private MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();
    @MockBean
    private ArticleService articleService;

    ArticleRequestDTO requestDTO;
    ArticleResponseDTO responseDTO;
    @BeforeEach
    void init(){
        requestDTO = new ArticleRequestDTO().builder()
                .title("테스트용 제목")
                .content("테스트용 내용")
                .build();

        responseDTO = new ArticleResponseDTO().builder()
                .id(1L)
                .writer("song@wanted.com")
                .title("테스트용 제목")
                .content("테스트용 내용")
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();

    }
    @Test
    void register() throws Exception{
        given(articleService.register(requestDTO)).willReturn(1L);

        mockMvc.perform(post("/api/article/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void read() throws Exception{
        given(articleService.read(1L)).willReturn(responseDTO);

        mockMvc.perform(get("/api/article/{id}","1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void update() throws Exception{
        ArticleResponseDTO updateDTO = new ArticleResponseDTO().builder()
                .id(1L)
                .writer("song@wanted.com")
                .title("테스트용 제목 수정")
                .content("테스트용 내용 수정")
                .updatedDate(LocalDateTime.now())
                .build();
        given(articleService.update(1L,requestDTO)).willReturn(updateDTO);

        mockMvc.perform(patch("/api/article/{id}","1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("테스트용 제목 수정"));
    }
    @Test
    void deleteTest() throws Exception{
        given(articleService.delete(1L)).willReturn(1L);

        mockMvc.perform(delete("/api/article/{id}","1")
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    void list() throws Exception {
        List<ArticleResponseDTO> list = new ArrayList<>();
        list.add(responseDTO);
        list.add(responseDTO);
        list.add(responseDTO);

        given(articleService.pageable(any()))
                .willReturn(list);

        mockMvc.perform(get("/api/article/pageable")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].title").value("테스트용 제목"))
                .andExpect(jsonPath("$[1].content").value("테스트용 내용"))
                .andExpect(jsonPath("$[2].writer").value("song@wanted.com"));
    }
}