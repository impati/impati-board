package com.example.impatiboard.interfaces.argument;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.impatiboard.error.BoardApiException;
import com.example.impatiboard.error.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerFetcher {

	private static final String CLIENT_ID = "clientId";
	private static final String CUSTOMER_ENDPOINT = "/api/v1/customer";

	private final RestTemplate restTemplate;

	@Value("${customer.clientId}")
	private String clientId;

	@Value("${customer.server}")
	private String server;

	public AuthenticationCustomer bringCustomer(final String token) {
		try {
			final ResponseEntity<AuthenticationCustomer> response = restTemplate.exchange(
				server + CUSTOMER_ENDPOINT,
				HttpMethod.POST,
				createRequestHeader(token),
				AuthenticationCustomer.class
			);
			return response.getBody();
		} catch (Exception e) {
			throw new BoardApiException(ErrorCode.UN_AUTHORIZATION);
		}
	}

	private HttpEntity createRequestHeader(final String accessToken) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set(CLIENT_ID, clientId);
		httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
		return new HttpEntity<>(httpHeaders);
	}
}
