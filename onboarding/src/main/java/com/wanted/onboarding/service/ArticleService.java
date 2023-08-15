package com.wanted.onboarding.service;

import com.wanted.onboarding.dto.ArticleRequestDTO;
import com.wanted.onboarding.dto.ArticleResponseDTO;
import com.wanted.onboarding.error.CommonErrorCode;
import com.wanted.onboarding.error.exception.NotFoundArticleException;
import com.wanted.onboarding.error.exception.NotFoundUserException;
import com.wanted.onboarding.error.exception.NotMatchedWriterException;
import com.wanted.onboarding.model.Article;
import com.wanted.onboarding.model.User;
import com.wanted.onboarding.repository.ArticleRepository;
import com.wanted.onboarding.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ArticleService {

    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;

    public Long register(ArticleRequestDTO DTO){

        String username = authenticationGetUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundUserException(CommonErrorCode.NOT_FOUND_USER));
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
    public List<ArticleResponseDTO> pageable(Pageable pageable){
        Page<Article> page = articleRepository.findAll(pageable);
        List<Article> list = page.getContent();
        return list.stream()
                .map(m -> new ArticleResponseDTO(m.getId(),
                        m.getTitle(),
                        m.getContent(),
                        m.getWriter().getUsername(),
                        m.getCreatedDate(),
                        m.getUpdatedDate()))
                .collect(Collectors.toList());
    }
    public ArticleResponseDTO update(Long id, ArticleRequestDTO DTO){

        Article article = articleRepository.findById(id).orElseThrow(()-> new NotFoundArticleException(CommonErrorCode.NOT_FOUND_ARTICLE));

        if(!checkWriter(article.getWriter().getUsername())){
            throw new NotMatchedWriterException(CommonErrorCode.NOT_MATCHED_WRITER);
        }

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
    public Long delete(Long id){
        if (!articleRepository.existsById(id)) {
            throw new NotFoundArticleException(CommonErrorCode.NOT_FOUND_ARTICLE);
        }
        Article article = articleRepository.findById(id).orElseThrow(()-> new NotFoundArticleException(CommonErrorCode.NOT_FOUND_ARTICLE));

        if(!checkWriter(article.getWriter().getUsername())){
            throw new NotMatchedWriterException(CommonErrorCode.NOT_MATCHED_WRITER);
        }

        articleRepository.delete(article);
        return id;
    }

    private boolean checkWriter(String writer){
        String username = authenticationGetUsername();
        if(writer.equals(username)) return true;
        return false;
    }

    private String authenticationGetUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
