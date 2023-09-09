package com.example.impatiboard.services.comment;

import static java.util.stream.Collectors.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.impatiboard.error.BoardApiException;
import com.example.impatiboard.error.ErrorCode;
import com.example.impatiboard.models.Article;
import com.example.impatiboard.models.Comment;
import com.example.impatiboard.repositories.CommentRepository;
import com.example.impatiboard.services.comment.dto.CommentResult;
import com.example.impatiboard.services.common.EntityFinder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CommentManager {

	private final EntityFinder entityFinder;
	private final CommentRepository commentRepository;

	public void register(
		final Long articleId,
		final Long customerId,
		final String content
	) {
		final Article article = entityFinder.find(articleId, Article.class);
		commentRepository.save(Comment.builder()
			.content(content)
			.customerId(customerId)
			.article(article)
			.build());
	}

	public List<CommentResult> search(final Long articleId, final Long customerId) {
		final Article article = entityFinder.find(articleId, Article.class);
		return commentRepository.findCommentByArticle(article)
			.stream()
			.map(comment -> CommentResult.of(comment, customerId))
			.collect(toList());
	}

	public void remove(final Long commentId, final Long customerId) {
		final Comment comment = entityFinder.find(commentId, Comment.class);
		validate(comment, customerId);
		commentRepository.delete(comment);
	}

	public CommentResult edit(
		final Long commentId,
		final Long customerId,
		final String content
	) {
		final Comment comment = entityFinder.find(commentId, Comment.class);
		validate(comment, customerId);
		comment.edit(content);
		return CommentResult.of(comment, customerId);
	}

	private void validate(final Comment comment, final Long customerId) {
		if (!comment.isWriter(customerId)) {
			throw new BoardApiException(ErrorCode.UN_AUTHORIZATION);
		}
	}
}
