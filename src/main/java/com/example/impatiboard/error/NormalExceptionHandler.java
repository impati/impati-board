package com.example.impatiboard.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.impatiboard.utils.ApiResult;

@RestControllerAdvice
public class NormalExceptionHandler {

	@ExceptionHandler(BoardApiException.class)
	public ApiResult<Void> boardException(final BoardApiException exception) {
		return ApiResult.failed(exception.getStatus(), exception.getMessage());
	}

	@ExceptionHandler({
		IllegalStateException.class,
		MethodArgumentNotValidException.class
	})
	public ApiResult<Void> argumentException(final Exception exception) {
		return ApiResult.failed(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
	}
}
