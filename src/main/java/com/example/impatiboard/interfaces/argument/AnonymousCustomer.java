package com.example.impatiboard.interfaces.argument;

public class AnonymousCustomer implements Customer {
    @Override
    public boolean isAuthenticated() {
        return false;
    }
}
