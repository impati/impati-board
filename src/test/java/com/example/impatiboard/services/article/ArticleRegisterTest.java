package com.example.impatiboard.services.article;

import static com.example.impatiboard.fixture.ArticleFixture.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.example.impatiboard.configure.JpaConfigure;
import com.example.impatiboard.models.Article;
import com.example.impatiboard.repositories.ArticleRepository;

@DataJpaTest
@Import({ArticleRegister.class, JpaConfigure.class})
class ArticleRegisterTest {

	@Autowired
	private ArticleRegister articleRegister;
    
	@Autowired
	private ArticleRepository articleRepository;

	@Test
	@DisplayName("게시글 저장 테스트")
	void registrationTest() {
		final Article article = createDefaults();

		articleRegister.registration(article);

		assertThat(articleRepository.findAll()).hasSize(1);
		assertThat(article.getCreatedAt()).isNotNull();
	}
}
