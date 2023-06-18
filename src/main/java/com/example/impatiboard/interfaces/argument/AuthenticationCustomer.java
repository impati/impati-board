package com.example.impatiboard.interfaces.argument;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationCustomer implements Customer {
    private Long id;
    private String username;
    private String nickname;
    private String email;
    private String blogUrl;
    private String profileImageUrl;
    private String introduceComment;

    @Override
    public boolean isAuthenticated() {
        return true;
    }
}
