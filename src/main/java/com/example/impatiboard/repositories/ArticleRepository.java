package com.example.impatiboard.repositories;

import com.example.impatiboard.models.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article,Long> {
}
