package com.example.impatiboard.models;

import com.example.impatiboard.fixture.ArticleFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ArticleTest {

    @Test
    @DisplayName("랜덤 Article 생성하기")
    public void ArticleFixtureCreate() throws Exception {
        Article article = ArticleFixture.createArticle();
    }
}
