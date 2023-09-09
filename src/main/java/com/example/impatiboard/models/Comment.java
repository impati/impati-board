package com.example.impatiboard.models;

import static java.time.LocalDateTime.*;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.util.Assert;

import com.example.impatiboard.models.common.BaseTimeEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
	public Comment(
		final Long customerId,
		final String content,
		final Article article,
		final LocalDateTime createdAt
	) {
		Assert.hasText(content, "댓글 내용은 필수입니다.");
		Assert.notNull(customerId, "댓글을 작성하기위해서는 사용자 ID가 필수입니다.");
		Assert.notNull(article, "댓글을 작성하기위해서는 게시글 정보는 필수입니다.");
		this.customerId = customerId;
		this.content = content;
		this.boardType = article.getBoardType();
		this.article = article;
		this.createdAt = createdAt == null ? now() : createdAt;
	}

	public void edit(final String content) {
		if (!StringUtils.isBlank(content)) {
			this.content = content;
		}
	}

	public boolean isWriter(final Long customerId) {
		return Objects.equals(this.customerId, customerId);
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Comment comment)) {
			return false;
		}

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
