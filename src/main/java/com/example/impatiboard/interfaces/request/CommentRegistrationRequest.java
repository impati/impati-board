package com.example.impatiboard.interfaces.request;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

public record CommentRegistrationRequest(
        @Length(max = 20000)
        @NotBlank
        String content
) {
}
