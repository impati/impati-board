package com.example.impatiboard.services.article;

import static com.example.impatiboard.fixture.ArticleFixture.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.example.impatiboard.configure.JpaConfigure;
import com.example.impatiboard.error.BoardApiException;
import com.example.impatiboard.models.Article;
import com.example.impatiboard.repositories.ArticleRepository;
import com.example.impatiboard.services.article.dto.ArticleEditCondition;
import com.example.impatiboard.services.common.EntityFinder;

@DataJpaTest
@Import({ArticleEditor.class,
	JpaConfigure.class,
	EntityFinder.class
})
class ArticleEditorTest {

	@Autowired
	private ArticleEditor articleEditor;

	@Autowired
	private ArticleRepository articleRepository;

	@Test
	@DisplayName("게시글 수정")
	void editArticleTest() {
		// given
		final Article article = createDefaults();
		articleRepository.save(article);
		final String title = "title";
		final String content = "content";
		final ArticleEditCondition condition = new ArticleEditCondition(article.getId(), title, content);

		// when
		articleEditor.edit(condition, article.getCustomerId());

		// then
		assertThat(article.getContent()).isEqualTo(content);
		assertThat(article.getTitle()).isEqualTo(title);
	}

	@Test
	@DisplayName("권한 없는 사용자의 게시글 수정")
	void editArticleTestCaseOfUnAuthorization() {
		// when
		final Article article = createDefaults();
		articleRepository.save(article);
		final String title = "title";
		final String content = "content";

		final ArticleEditCondition condition = new ArticleEditCondition(article.getId(), title, content);

		// expected
		assertThatCode(() -> articleEditor.edit(condition, DEFAULT_CUSTOMER_ID + 2L))
			.isInstanceOf(BoardApiException.class);
	}
}
