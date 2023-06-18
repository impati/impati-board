package com.example.impatiboard.interfaces.response;


import com.example.impatiboard.services.comment.dto.CommentResult;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record CommentResponse(
        Long commentId,
        String content,
        String nickname,
        Long articleId,
        LocalDate createdAt,
        boolean isWritten
) {
    public static CommentResponse from(CommentResult commentResult) {
        return CommentResponse.builder()
                .commentId(commentResult.commentId())
                .content(commentResult.content())
                .nickname(commentResult.nickname())
                .articleId(commentResult.articleId())
                .createdAt(commentResult.createdAt())
                .isWritten(commentResult.isWritten())
                .build();
    }
}
