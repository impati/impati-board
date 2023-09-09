package com.example.impatiboard.interfaces.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.example.impatiboard.models.Article;
import com.example.impatiboard.models.BoardType;
import com.example.impatiboard.models.ClientType;

public record ArticleRegistrationRequest(
	@Length(max = 200)
	@NotBlank
	String title,
	@Length(max = 20000)
	@NotBlank
	String content,
	@NotNull
	BoardType boardType,
	@NotNull
	ClientType clientType
) {

	public Article toEntity(final Long customerId, final String nickname) {
		return Article.builder()
			.clientType(clientType)
			.nickname(nickname)
			.title(title)
			.content(content)
			.boardType(boardType)
			.customerId(customerId)
			.build();
	}
}
