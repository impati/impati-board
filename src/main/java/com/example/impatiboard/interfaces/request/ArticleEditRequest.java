package com.example.impatiboard.interfaces.request;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import com.example.impatiboard.services.article.dto.ArticleEditCondition;

public record ArticleEditRequest(
	@Length(max = 200)
	@NotBlank
	String title,
	@Length(max = 20000)
	@NotBlank
	String content
) {
	public ArticleEditCondition toCondition(final Long articleId) {
		return new ArticleEditCondition(
			articleId,
			title,
			content
		);
	}
}
