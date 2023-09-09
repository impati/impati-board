package com.example.impatiboard.services.article;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.impatiboard.error.BoardApiException;
import com.example.impatiboard.error.ErrorCode;
import com.example.impatiboard.models.Article;
import com.example.impatiboard.repositories.ArticleRepository;
import com.example.impatiboard.services.common.EntityFinder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ArticleRemover {
	private final EntityFinder entityFinder;
	private final ArticleRepository articleRepository;

	public void remove(final Long articleId, final Long customerId) {
		final Article article = entityFinder.find(articleId, Article.class);
		validate(article, customerId);
		articleRepository.delete(article);
	}

	private void validate(final Article article, final Long customerId) {
		if (!article.isWriter(customerId)) {
			throw new BoardApiException(ErrorCode.UN_AUTHORIZATION);
		}
	}
}
