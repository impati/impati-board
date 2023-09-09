package com.example.impatiboard.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class ApiResult<T> {

	private T data;

	private ErrorData error;

	ApiResult(final T data) {
		this.data = data;
	}

	ApiResult(final int status, final String message) {
		this.data = null;
		this.error = new ErrorData(status, message);
	}

	public static <T> ApiResult<T> succeed(final T data) {
		return new ApiResult<>(data);
	}

	public static ApiResult<String> succeed() {
		return new ApiResult<>("Success");
	}

	public static <T> ApiResult<T> failed(final int status, final String message) {
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
