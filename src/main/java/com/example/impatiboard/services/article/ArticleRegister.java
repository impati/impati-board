package com.example.impatiboard.services.article;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.impatiboard.models.Article;
import com.example.impatiboard.repositories.ArticleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleRegister {
	private final ArticleRepository articleRepository;

	@Transactional
	public void registration(final Article article) {
		articleRepository.save(article);
	}
}
