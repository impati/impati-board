package com.example.impatiboard.interfaces.response;

import com.example.impatiboard.interfaces.argument.AuthenticationCustomer;
import com.example.impatiboard.interfaces.argument.Customer;
import com.example.impatiboard.models.Article;
import com.example.impatiboard.models.BoardType;
import com.example.impatiboard.models.ClientType;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record ArticleSpecificResponse(
        Long articleId,
        String title,
        String content,
        BoardType boardType,
        ClientType clientType,
        LocalDate createdAt,
        String nickname,
        boolean isWritten
) {
    public static ArticleSpecificResponse of(Article article, Customer customer) {
        if (!customer.isAuthenticated()) return from(article);
        AuthenticationCustomer authenticationCustomer = (AuthenticationCustomer) customer;
        return defaults(article)
                .isWritten(article.getCustomerId().equals(authenticationCustomer.getId()))
                .build();
    }

    private static ArticleSpecificResponse from(Article article) {
        return defaults(article)
                .build();
    }

    private static ArticleSpecificResponse.ArticleSpecificResponseBuilder defaults(Article article) {
        return ArticleSpecificResponse.builder()
                .articleId(article.getId())
                .title(article.getTitle())
                .boardType(article.getBoardType())
                .clientType(article.getClientType())
                .nickname(article.getCreatedBy())
                .createdAt(article.getCreatedAt().toLocalDate())
                .content(article.getContent())
                .isWritten(false);
    }
}
