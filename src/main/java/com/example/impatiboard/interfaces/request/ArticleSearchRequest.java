package com.example.impatiboard.interfaces.request;

import com.example.impatiboard.models.BoardType;
import com.example.impatiboard.models.ClientType;
import com.example.impatiboard.services.article.dto.ArticleSearchCondition;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public record ArticleSearchRequest(
        String titleName,
        BoardType boardType,
        ClientType clientType,
        String nickname,
        LocalDate createdDate
) {

    public ArticleSearchCondition toCondition(Pageable pageable) {
        return ArticleSearchCondition.builder()
                .titleName(titleName)
                .boardType(boardType)
                .clientType(clientType)
                .createdDate(createdDate)
                .nickname(nickname)
                .pageable(pageable)
                .build();
    }
}
