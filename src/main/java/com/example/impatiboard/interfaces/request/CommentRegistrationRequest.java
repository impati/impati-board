package com.example.impatiboard.interfaces.request;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

public record CommentRegistrationRequest(
	@Length(max = 20000)
	@NotBlank
	String content
) {
}
