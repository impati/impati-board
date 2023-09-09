package com.example.impatiboard.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.impatiboard.models.Article;
import com.example.impatiboard.models.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

	@EntityGraph(attributePaths = {"article"})
	List<Comment> findCommentByArticle(final Article article);
}
