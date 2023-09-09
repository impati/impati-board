package com.example.impatiboard.services.article.dto;

public record ArticleEditCondition(

	Long articleId,
	String title,
	String content
) {
}
