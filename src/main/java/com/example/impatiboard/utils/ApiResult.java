package com.example.impatiboard.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class ApiResult<T> {

    private T data;

    private ErrorData error;

    ApiResult(T data) {
        this.data = data;
    }

    ApiResult(int status, String message) {
        this.data = null;
        this.error = new ErrorData(status, message);
    }

    public static <T> ApiResult<T> succeed(T data) {
        return new ApiResult<>(data);
    }

    public static ApiResult<String> succeed() {
        return new ApiResult<>("Success");
    }

    public static <T> ApiResult<T> failed(int status, String message) {
        return new ApiResult<>(status, message);
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    static class ErrorData {
        private int status;
        private String message;
    }
}
