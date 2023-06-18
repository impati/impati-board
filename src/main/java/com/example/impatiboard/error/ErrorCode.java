package com.example.impatiboard.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    NOT_SUPPORT_BOARD_TYPE(HttpStatus.BAD_REQUEST.value(), "지원하지 않는 BoardType 입니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND.value(), "찾을 수 없는 게시글 또는 댓글 정보입니다."),
    UN_AUTHORIZATION(HttpStatus.UNAUTHORIZED.value(), "권한이 없습니다."),
    INVALID_TOKEN(HttpStatus.FORBIDDEN.value(), "토큰이 없거나 정보가 올바르지 않습니다");

    private final int status;
    private final String message;
}
