package com.example.impatiboard.interfaces.request;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

public record CommentEditRequest(
	@Length(max = 2000)
	@NotBlank
	String content
) {
}
