package com.example.impatiboard.interfaces.argument;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.example.impatiboard.error.BoardApiException;
import com.example.impatiboard.error.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomerArgumentResolver implements HandlerMethodArgumentResolver {

	private static final String AUTHORIZATION_HEADER_NAME = "X-AUTH";
	private final CustomerFetcher customerFetcher;

	@Override
	public boolean supportsParameter(final MethodParameter parameter) {
		return Customer.class.isAssignableFrom(parameter.getParameterType());
	}

	@Override
	public Object resolveArgument(
		final MethodParameter parameter,
		final ModelAndViewContainer mavContainer,
		final NativeWebRequest webRequest,
		final WebDataBinderFactory binderFactory
	) {
		if (nonExistToken(webRequest)) {
			return new AnonymousCustomer();
		}
		final String token = getToken(webRequest);
		final AuthenticationCustomer response = customerFetcher.bringCustomer(token);
		if (response == null) {
			throw new BoardApiException(ErrorCode.INVALID_TOKEN);
		}
		return response;
	}

	private boolean nonExistToken(final NativeWebRequest webRequest) {
		final String token = webRequest.getHeader(AUTHORIZATION_HEADER_NAME);
		return StringUtils.isBlank(token);
	}

	private String getToken(final NativeWebRequest webRequest) {
		return webRequest.getHeader(AUTHORIZATION_HEADER_NAME);
	}
}
