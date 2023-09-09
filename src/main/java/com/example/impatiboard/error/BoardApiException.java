package com.example.impatiboard.error;

import lombok.Getter;

@Getter
public class BoardApiException extends RuntimeException {

    private final String message;
    private final int status;

    public BoardApiException(final ErrorCode errorCode) {
        this.message = errorCode.getMessage();
        this.status = errorCode.getStatus();
    }
}
