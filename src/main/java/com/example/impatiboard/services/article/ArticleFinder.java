package com.example.impatiboard.services.article;

import com.example.impatiboard.models.Article;
import com.example.impatiboard.repositories.ArticleSearchRepository;
import com.example.impatiboard.services.article.dto.ArticleSearchCondition;
import com.example.impatiboard.services.common.EntityFinder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleFinder {
    private final ArticleSearchRepository articleSearchRepository;
    private final EntityFinder entityFinder;

    public Page<Article> search(ArticleSearchCondition condition) {
        return articleSearchRepository.search(condition);
    }

    @Transactional
    public Article search(Long articleId) {
        return entityFinder.find(articleId, Article.class);
    }
}
