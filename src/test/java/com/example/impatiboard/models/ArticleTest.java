package com.example.impatiboard.models;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ArticleTest {

	@Test
	@DisplayName("게시글의 작성자라면 true 를 반환한다.")
	void isWriter() {
		final Long customerId = 1L;
		final Article article = getArticle(customerId);

		assertThat(article.isWriter(1L)).isTrue();
	}

	@Test
	@DisplayName("게시글의 작성자라면 true 를 반환한다.")
	void isNoWriter() {
		final Long customerId = 1L;
		final Article article = getArticle(customerId);

		assertThat(article.isWriter(2L)).isFalse();
	}

	@Test
	@DisplayName("제목 수정")
	void editTitle() {
		final String newTitle = "newHello";
		final Article article = getArticle();
		final String originContent = article.getContent();

		article.edit(newTitle, "");

		assertThat(article.getTitle()).isEqualTo(newTitle);
		assertThat(article.getContent()).isEqualTo(originContent);
	}

	@Test
	@DisplayName("제목 수정")
	void editContent() {
		final String newContent = "newHello";
		final Article article = getArticle();
		final String originTitle = article.getTitle();

		article.edit(null, newContent);

		assertThat(article.getTitle()).isEqualTo(originTitle);
		assertThat(article.getContent()).isEqualTo(newContent);
	}

	private Article getArticle(final Long customerId) {
		return defaults()
			.customerId(customerId)
			.build();
	}

	private Article getArticle() {
		return defaults()
			.build();
	}

	private Article.ArticleBuilder defaults() {
		return Article.builder()
			.title("제목")
			.customerId(1L)
			.content("내용")
			.boardType(BoardType.SUGGESTION)
			.clientType(ClientType.SERVICE_HUB);
	}
}
