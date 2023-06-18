package com.example.impatiboard.repositories;

import com.example.impatiboard.models.Article;
import com.example.impatiboard.models.Comment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @EntityGraph(attributePaths = {"article"})
    List<Comment> findCommentByArticle(Article article);
}
