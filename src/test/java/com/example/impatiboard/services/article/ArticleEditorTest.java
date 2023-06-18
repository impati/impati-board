package com.example.impatiboard.services.article;

import com.example.impatiboard.configure.JpaConfigure;
import com.example.impatiboard.error.BoardApiException;
import com.example.impatiboard.models.Article;
import com.example.impatiboard.repositories.ArticleRepository;
import com.example.impatiboard.services.article.dto.ArticleEditCondition;
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
    public void editArticleTest() throws Exception {
        // given
        Article article = createDefaults();
        articleRepository.save(article);
        String title = "title";
        String content = "content";

        ArticleEditCondition condition = new ArticleEditCondition(article.getId(), title, content);

        // when
        articleEditor.edit(condition, article.getCustomerId());

        // then
        assertThat(article.getContent()).isEqualTo(content);
        assertThat(article.getTitle()).isEqualTo(title);
    }

    @Test
    @DisplayName("권한 없는 사용자의 게시글 수정")
    public void editArticleTestCaseOfUnAuthorization() throws Exception {
        // when
        Article article = createDefaults();
        articleRepository.save(article);
        String title = "title";
        String content = "content";

        ArticleEditCondition condition = new ArticleEditCondition(article.getId(), title, content);

        // expected
        assertThatCode(() -> articleEditor.edit(condition, DEFAULT_CUSTOMER_ID + 2L))
                .isInstanceOf(BoardApiException.class);

    }
}