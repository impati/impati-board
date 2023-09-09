package com.example.impatiboard.repositories;

import static com.example.impatiboard.models.QArticle.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Repository;

import com.example.impatiboard.models.Article;
import com.example.impatiboard.models.BoardType;
import com.example.impatiboard.models.ClientType;
import com.example.impatiboard.services.article.dto.ArticleSearchCondition;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ArticleSearchRepository {

	private final JPAQueryFactory jpaQueryFactory;

	public Page<Article> search(final ArticleSearchCondition condition) {
		final List<Article> result = jpaQueryFactory
			.select(article)
			.from(article)
			.where(ifTitle(condition.titleName()),
				ifBoardType(condition.boardType()),
				ifNickname(condition.nickname()),
				ifCreatedAt(condition.createdDate()),
				ifClientType(condition.clientType()))
			.orderBy(article.createdAt.desc())
			.offset(condition.pageable().getOffset())
			.limit(condition.pageable().getPageSize())
			.fetch();
		return new PageImpl<>(result, condition.pageable(), computeTotalCount(condition));
	}

	private long computeTotalCount(final ArticleSearchCondition condition) {
		return jpaQueryFactory
			.select(article)
			.from(article)
			.where(ifTitle(condition.titleName()),
				ifBoardType(condition.boardType()),
				ifNickname(condition.nickname()),
				ifCreatedAt(condition.createdDate()))
			.fetch().size();
	}

	private BooleanExpression ifClientType(final ClientType clientType) {
		if (clientType == null) {
			return null;
		}

		return article.clientType.eq(clientType);
	}

	private BooleanExpression ifCreatedAt(final LocalDate createdAt) {
		if (createdAt == null) {
			return null;
		}

		return article.createdAt.goe(LocalDateTime.of(createdAt, LocalTime.MIN));
	}

	private BooleanExpression ifTitle(final String title) {
		return StringUtils.isBlank(title) ? null : article.title.containsIgnoreCase(title);
	}

	private BooleanExpression ifNickname(final String nickname) {
		return StringUtils.isBlank(nickname) ? null : article.createdBy.containsIgnoreCase(nickname);
	}

	private BooleanExpression ifBoardType(final BoardType boardType) {
		if (boardType == null) {
			return null;
		}

		return article.boardType.eq(boardType);
	}
}
