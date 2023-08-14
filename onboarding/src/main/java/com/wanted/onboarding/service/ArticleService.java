package com.wanted.onboarding.service;

import com.wanted.onboarding.dto.ArticleRequestDTO;
import com.wanted.onboarding.dto.ArticleResponseDTO;
import com.wanted.onboarding.error.CommonErrorCode;
import com.wanted.onboarding.error.exception.NotFoundArticleException;
import com.wanted.onboarding.error.exception.NotMatchedWriterException;
import com.wanted.onboarding.model.Article;
import com.wanted.onboarding.model.User;
import com.wanted.onboarding.repository.ArticleRepository;
import com.wanted.onboarding.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
@Service
@Transactional
@RequiredArgsConstructor
public class ArticleService {

    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;

    public Long register(ArticleRequestDTO DTO){

        String username = authenticationGetUsername();
        User user = userRepository.findByUsername(username).orElseThrow();
        Article article = new Article().builder()
                .content(DTO.getContent())
                .title(DTO.getTitle())
                .writer(user)
                .build();
        articleRepository.save(article);

        return article.getId();
    }
    public ArticleResponseDTO read(Long id){
        Article article = articleRepository.findById(id).orElseThrow(()-> new NotFoundArticleException(CommonErrorCode.NOT_FOUND_ARTICLE));

        return new ArticleResponseDTO().builder()
                .id(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .writer(article.getWriter().getUsername())
                .createdDate(article.getCreatedDate())
                .updatedDate(article.getUpdatedDate())
                .build();
    }
    public ArticleResponseDTO update(Long id, ArticleRequestDTO DTO){
        Article article = articleRepository.findById(id).orElseThrow(()-> new NotFoundArticleException(CommonErrorCode.NOT_FOUND_ARTICLE));
        article.update(DTO);

        return new ArticleResponseDTO().builder()
                .id(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .writer(article.getWriter().getUsername())
                .createdDate(article.getCreatedDate())
                .updatedDate(article.getUpdatedDate())
                .build();
    }
    public void delete(Long id){
        if (!articleRepository.existsById(id)) {
            throw new NotFoundArticleException(CommonErrorCode.NOT_FOUND_ARTICLE);
        }
        Article article = articleRepository.findById(id).orElseThrow(()-> new NotFoundArticleException(CommonErrorCode.NOT_FOUND_ARTICLE));

        String username = authenticationGetUsername();
        if(!article.getWriter().getUsername().equals(username)) throw new NotMatchedWriterException(CommonErrorCode.NOT_MATCHED_WRITER);

        articleRepository.delete(article);
    }

    private String authenticationGetUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
