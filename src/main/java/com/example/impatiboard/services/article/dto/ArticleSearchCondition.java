package com.example.impatiboard.services.article.dto;

import java.time.LocalDate;

import org.springframework.data.domain.Pageable;

import com.example.impatiboard.models.BoardType;
import com.example.impatiboard.models.ClientType;

import lombok.Builder;

@Builder
public record ArticleSearchCondition(

	String titleName,
	BoardType boardType,
	ClientType clientType,
	String nickname,
	LocalDate createdDate,
	Pageable pageable
) {
}
