package com.wanted.onboarding.repository;

import com.wanted.onboarding.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article,Long> {
}
