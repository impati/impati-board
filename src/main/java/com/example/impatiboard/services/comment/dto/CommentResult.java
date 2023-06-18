package com.example.impatiboard.services.comment.dto;

import com.example.impatiboard.models.Comment;

import java.time.LocalDate;

public record CommentResult(
        Long commentId,
        String content,
        String nickname,
        Long articleId,
        LocalDate createdAt,
        boolean isWritten
) {
    public static CommentResult of(Comment comment, Long customerId) {
        return new CommentResult(
                comment.getId(),
                comment.getContent(),
                comment.getArticle().getCreatedBy(),
                comment.getId(),
                comment.getCreatedAt().toLocalDate(),
                isSameWriter(comment, customerId));
    }

    private static boolean isSameWriter(Comment comment, Long customerId) {
        if (customerId == null) return false;
        return comment.getCustomerId().equals(customerId);
    }
}
