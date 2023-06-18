package com.example.impatiboard.services.article;

import com.example.impatiboard.error.BoardApiException;
import com.example.impatiboard.error.ErrorCode;
import com.example.impatiboard.models.Article;
import com.example.impatiboard.services.article.dto.ArticleEditCondition;
import com.example.impatiboard.services.common.EntityFinder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ArticleEditor {
    private final EntityFinder entityFinder;

    public Article edit(ArticleEditCondition condition, Long customerId) {
        Article article = entityFinder.find(condition.articleId(), Article.class);
        validate(article, customerId);
        article.edit(condition.title(), condition.content());
        return article;
    }

    private void validate(Article article, Long customerId) {
        if (!isSameWriter(article, customerId)) throw new BoardApiException(ErrorCode.NOT_SUPPORT_BOARD_TYPE);
    }

    private boolean isSameWriter(Article article, Long customerId) {
        return article.getCustomerId().equals(customerId);
    }
}
