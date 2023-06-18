package com.example.impatiboard.models;

import com.example.impatiboard.models.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

import static java.time.LocalDateTime.now;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(nullable = false)
    private Long customerId;

    @Column(nullable = false, length = 2000)
    private String content;

    @Enumerated(EnumType.STRING)
    private BoardType boardType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    @Builder
    public Comment(Long customerId, String content, Article article, LocalDateTime createdAt) {
        Assert.hasText(content, "댓글 내용은 필수입니다.");
        Assert.notNull(customerId, "댓글을 작성하기위해서는 사용자 ID가 필수입니다.");
        Assert.notNull(article, "댓글을 작성하기위해서는 게시글 정보는 필수입니다.");
        this.customerId = customerId;
        this.content = content;
        this.boardType = article.getBoardType();
        this.article = article;
        this.createdAt = createdAt == null ? now() : createdAt;
    }

    public void edit(String content) {
        if (!StringUtils.isBlank(content)) this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment comment)) return false;
        return this.getId() != null && Objects.equals(id, comment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("customerId", customerId)
                .append("boardType", boardType)
                .toString();
    }

}
