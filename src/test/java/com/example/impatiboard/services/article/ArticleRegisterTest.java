package com.example.impatiboard.services.article;

import com.example.impatiboard.configure.JpaConfigure;
import com.example.impatiboard.models.Article;
import com.example.impatiboard.repositories.ArticleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static com.example.impatiboard.fixture.ArticleFixture.createDefaults;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({ArticleRegister.class, JpaConfigure.class})
class ArticleRegisterTest {

    @Autowired
    private ArticleRegister articleRegister;
    @Autowired
    private ArticleRepository articleRepository;

    @Test
    @DisplayName("게시글 저장 테스트")
    public void registrationTest() throws Exception {
        // given
        Article article = createDefaults();

        // when
        articleRegister.registration(article);

        // then
        assertThat(articleRepository.findAll().size())
                .isEqualTo(1);

        assertThat(article.getCreatedAt())
                .isNotNull();
    }


}