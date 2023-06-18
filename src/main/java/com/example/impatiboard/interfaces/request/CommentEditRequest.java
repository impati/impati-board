package com.example.impatiboard.interfaces.request;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

public record CommentEditRequest(
        @Length(max = 2000)
        @NotBlank
        String content
) {
}
