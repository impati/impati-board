package com.example.impatiboard.models;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
public class Article extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "article_id")
	private Long id;

	@Column(nullable = false)
	private Long customerId;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false, length = 2000)
	private String content;

	@Enumerated(EnumType.STRING)
	private BoardType boardType;

	@Enumerated(EnumType.STRING)
	private ClientType clientType;

	@Column(nullable = false)
	private String createdBy;

	@Builder
	public Article(
		final Long customerId,
		final String title,
		final String content,
		final BoardType boardType,
		final String nickname,
		final LocalDateTime createAt,
		final LocalDateTime updatedAt,
		final ClientType clientType
	) {
		Assert.notNull(customerId, "게시글 생성을 위해서는 사용자 ID는 필수 입니다.");
		Assert.hasText(title, "게시글 제목은 필수입니다.");
		Assert.hasText(content, "게시글 내용은 필수입니다.");
		Assert.notNull(boardType, "게시글 타입은 필수입니다.");
		Assert.notNull(clientType, "클라이언트 타입은 필수입니다.");
		this.customerId = customerId;
		this.title = title;
		this.content = content;
		this.boardType = boardType;
		this.createdBy = nickname;
		this.createdAt = createAt;
		this.updatedAt = updatedAt;
		this.clientType = clientType;
	}

	public void edit(final String title, final String content) {
		if (!StringUtils.isBlank(title)) {
			this.title = title;
		}

		if (!StringUtils.isBlank(content)) {
			this.content = content;
		}
	}

	public boolean isWriter(final Long customerId) {
		return Objects.equals(this.customerId, customerId);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Article article)) {
			return false;
		}

		return this.getId() != null && Objects.equals(id, article.id);
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
			.append("title", title)
			.append("content", content)
			.append("boardType", boardType)
			.append("clientType", clientType)
			.append("createdAt", createdAt)
			.append("nickname", createdBy)
			.toString();
	}
}
