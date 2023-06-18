package com.example.impatiboard.services.article;

import com.example.impatiboard.models.Article;
import com.example.impatiboard.repositories.ArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleRegister {
    private final ArticleRepository articleRepository;

    @Transactional
    public void registration(Article article) {
        articleRepository.save(article);
    }
}
