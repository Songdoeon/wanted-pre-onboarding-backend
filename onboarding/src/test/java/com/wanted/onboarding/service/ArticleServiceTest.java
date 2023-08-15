package com.wanted.onboarding.service;

import com.wanted.onboarding.dto.ArticleRequestDTO;
import com.wanted.onboarding.dto.ArticleResponseDTO;
import com.wanted.onboarding.model.Article;
import com.wanted.onboarding.model.User;
import com.wanted.onboarding.repository.ArticleRepository;
import com.wanted.onboarding.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
@ExtendWith({MockitoExtension.class, SpringExtension.class})
@WithMockUser(username = "song@wanted.com")
class ArticleServiceTest {
    @Spy
    ArticleRepository articleRepository;
    @Spy
    UserRepository userRepository;
    @InjectMocks
    ArticleService articleService;
    ArticleRequestDTO articleRequestDTO;
    Article article;
    User user;

    @BeforeEach
    void init(){
        user = new User().builder()
                .username("song@wanted.com")
                .password("12341234")
                .roles("ROLE_USER")
                .build();

        articleRequestDTO = new ArticleRequestDTO().builder()
                .title("테스트용 제목")
                .content("테스트용 내용")
                .build();

        article = new Article().builder()
                .content(articleRequestDTO.getContent())
                .title(articleRequestDTO.getTitle())
                .writer(user)
                .build();
    }
    @Test
    @DisplayName("게시물 등록")
    void register() {
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));

        Long expect = articleService.register(articleRequestDTO);

        assertThat(article.getId()).isEqualTo(expect);
    }

    @Test
    @DisplayName("게시물 조회")
    void read() {
        when(articleRepository.findById(any())).thenReturn(Optional.of(article));

        ArticleResponseDTO actual = articleService.read(article.getId());

        assertThat(actual.getId()).isEqualTo(article.getId());
    }

    @Test
    @DisplayName("게시물 수정")
    void update() {
        ArticleRequestDTO updateDTO = new ArticleRequestDTO().builder()
                .title("테스트용 제목 수정")
                .content("테스트용 내용 수정")
                .build();
        when(articleRepository.findById(any())).thenReturn(Optional.of(article));

        ArticleResponseDTO actual = articleService.update(article.getId(),updateDTO);

        assertThat(articleRequestDTO.getTitle()).isNotEqualTo(actual.getTitle());
    }

    @Test
    @DisplayName("게시물 삭제")
    void delete() {
        when(articleRepository.existsById(any())).thenReturn(true);
        when(articleRepository.findById(any())).thenReturn(Optional.of(article));

        assertThat(articleService.delete(article.getId())).isEqualTo(article.getId());
    }
}