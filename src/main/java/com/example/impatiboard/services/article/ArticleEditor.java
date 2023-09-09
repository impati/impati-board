package com.example.impatiboard.services.article;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.impatiboard.error.BoardApiException;
import com.example.impatiboard.error.ErrorCode;
import com.example.impatiboard.models.Article;
import com.example.impatiboard.services.article.dto.ArticleEditCondition;
import com.example.impatiboard.services.common.EntityFinder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ArticleEditor {

	private final EntityFinder entityFinder;

	public Article edit(final ArticleEditCondition condition, final Long customerId) {
		final Article article = entityFinder.find(condition.articleId(), Article.class);
		validate(article, customerId);
		article.edit(condition.title(), condition.content());

		return article;
	}

	private void validate(final Article article, final Long customerId) {
		if (!article.isWriter(customerId)) {
			throw new BoardApiException(ErrorCode.UN_AUTHORIZATION);
		}
	}
}
