package com.example.impatiboard.services.article.dto;

import com.example.impatiboard.models.BoardType;
import com.example.impatiboard.models.ClientType;
import lombok.Builder;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

@Builder
public record ArticleSearchCondition(
        String titleName,
        BoardType boardType,
        ClientType clientType,
        String nickname,
        LocalDate createdDate,
        Pageable pageable
) {
}
