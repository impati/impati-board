package com.example.impatiboard.interfaces.argument;

import com.example.impatiboard.error.BoardApiException;
import com.example.impatiboard.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerFetcher {
    private final static String CLIENT_ID = "clientId";
    private final static String CUSTOMER_ENDPOINT = "/api/v1/customer";
    private final RestTemplate restTemplate;
    @Value("${customer.clientId}")
    private String clientId;

    @Value("${customer.server}")
    private String server;

    public AuthenticationCustomer bringCustomer(String token) {
        try {
            return restTemplate.exchange(server + CUSTOMER_ENDPOINT, HttpMethod.POST, createRequestHeader(token), AuthenticationCustomer.class).getBody();
        } catch (Exception e) {
            throw new BoardApiException(ErrorCode.UN_AUTHORIZATION);
        }
    }

    private HttpEntity createRequestHeader(String accessToken) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(CLIENT_ID, clientId);
        httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        return new HttpEntity<>(httpHeaders);
    }
}
