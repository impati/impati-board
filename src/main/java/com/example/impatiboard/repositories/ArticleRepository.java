package com.example.impatiboard.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.impatiboard.models.Article;

public interface ArticleRepository extends JpaRepository<Article, Long> {
	
}
