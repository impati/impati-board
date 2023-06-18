package com.example.impatiboard.services.article;

import com.example.impatiboard.configure.JpaConfigure;
import com.example.impatiboard.error.BoardApiException;
import com.example.impatiboard.models.Article;
import com.example.impatiboard.repositories.ArticleRepository;
import com.example.impatiboard.services.common.EntityFinder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static com.example.impatiboard.fixture.ArticleFixture.DEFAULT_CUSTOMER_ID;
import static com.example.impatiboard.fixture.ArticleFixture.createDefaults;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@DataJpaTest
@Import({ArticleRemover.class,
        JpaConfigure.class,
        EntityFinder.class
})
class ArticleRemoverTest {

    @Autowired
    private ArticleRemover articleRemover;

    @Autowired
    private ArticleRepository articleRepository;

    @Test
    @DisplayName("게시글 삭제")
    public void removeArticle() throws Exception {
        // given
        Article article = createDefaults();
        articleRepository.save(article);

        // when
        articleRemover.remove(article.getId(), article.getCustomerId());

        // then
        assertThat(articleRepository.count()).isEqualTo(0);

    }

    @Test
    @DisplayName("권한 없는 사용자의 게시글 삭제")
    public void removeArticleTestCaseOfUnAuthorization() throws Exception {
        // when
        Article article = createDefaults();
        articleRepository.save(article);

        // expected
        assertThatCode(() -> articleRemover.remove(article.getId(), DEFAULT_CUSTOMER_ID + 2L))
                .isInstanceOf(BoardApiException.class);

    }
}