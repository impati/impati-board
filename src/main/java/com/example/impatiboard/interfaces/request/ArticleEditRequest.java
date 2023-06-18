package com.example.impatiboard.interfaces.request;

import com.example.impatiboard.services.article.dto.ArticleEditCondition;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

public record ArticleEditRequest(
        @Length(max = 200)
        @NotBlank
        String title,
        @Length(max = 20000)
        @NotBlank
        String content
) {
    public ArticleEditCondition toCondition(Long articleId) {
        return new ArticleEditCondition(
                articleId,
                title,
                content
        );
    }
}
