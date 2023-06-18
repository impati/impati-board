package com.example.impatiboard.services.comment;

import com.example.impatiboard.error.BoardApiException;
import com.example.impatiboard.error.ErrorCode;
import com.example.impatiboard.models.Article;
import com.example.impatiboard.models.Comment;
import com.example.impatiboard.repositories.CommentRepository;
import com.example.impatiboard.services.comment.dto.CommentResult;
import com.example.impatiboard.services.common.EntityFinder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CommentManager {
    private final EntityFinder entityFinder;
    private final CommentRepository commentRepository;

    public void register(Long articleId, Long customerId, String content) {
        Article article = entityFinder.find(articleId, Article.class);
        commentRepository.save(Comment.builder()
                .content(content)
                .customerId(customerId)
                .article(article)
                .build());
    }

    public List<CommentResult> search(Long articleId, Long customerId) {
        Article article = entityFinder.find(articleId, Article.class);
        return commentRepository.findCommentByArticle(article)
                .stream()
                .map(comment -> CommentResult.of(comment, customerId))
                .collect(toList());
    }

    public void remove(Long commentId, Long customerId) {
        Comment comment = entityFinder.find(commentId, Comment.class);
        validate(comment, customerId);
        commentRepository.delete(comment);
    }

    public CommentResult edit(Long commentId, Long customerId, String content) {
        Comment comment = entityFinder.find(commentId, Comment.class);
        validate(comment, customerId);
        comment.edit(content);
        return CommentResult.of(comment, customerId);
    }

    private void validate(Comment comment, Long customerId) {
        if (!comment.getCustomerId().equals(customerId)) throw new BoardApiException(ErrorCode.UN_AUTHORIZATION);
    }

}
