package com.example.impatiboard.interfaces.response;

import com.example.impatiboard.models.Article;
import com.example.impatiboard.models.BoardType;
import com.example.impatiboard.models.ClientType;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record ArticleResponse(
        Long articleId,
        String title,
        BoardType boardType,
        ClientType clientType,
        LocalDate createdAt,
        String nickname
) {
    public static ArticleResponse from(Article article) {
        return ArticleResponse.builder()
                .articleId(article.getId())
                .title(article.getTitle())
                .boardType(article.getBoardType())
                .clientType(article.getClientType())
                .nickname(article.getCreatedBy())
                .createdAt(article.getCreatedAt().toLocalDate())
                .build();
    }
}
