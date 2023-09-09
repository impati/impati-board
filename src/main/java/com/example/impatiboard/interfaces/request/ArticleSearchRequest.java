package com.example.impatiboard.interfaces.request;

import java.time.LocalDate;

import org.springframework.data.domain.Pageable;

import com.example.impatiboard.models.BoardType;
import com.example.impatiboard.models.ClientType;
import com.example.impatiboard.services.article.dto.ArticleSearchCondition;

public record ArticleSearchRequest(
	String titleName,
	BoardType boardType,
	ClientType clientType,
	String nickname,
	LocalDate createdDate
) {

	public ArticleSearchCondition toCondition(final Pageable pageable) {
		return ArticleSearchCondition.builder()
			.titleName(titleName)
			.boardType(boardType)
			.clientType(clientType)
			.createdDate(createdDate)
			.nickname(nickname)
			.pageable(pageable)
			.build();
	}
}
