package com.example.impatiboard.fixture;

import com.example.impatiboard.interfaces.argument.AuthenticationCustomer;

public class CustomerFixture {
    public static AuthenticationCustomer createCustomer(Long customerId) {
        return new AuthenticationCustomer(
                customerId,
                "impati",
                "impati",
                "yongs170@naver.com",
                "https://impati.github.io/",
                "https://impati.github.io/",
                "안녕하세요"
        );
    }
}
