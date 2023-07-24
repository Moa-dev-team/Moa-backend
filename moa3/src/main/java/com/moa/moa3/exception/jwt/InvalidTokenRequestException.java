package com.moa.moa3.exception.jwt;

public class InvalidTokenRequestException extends RuntimeException {
    public InvalidTokenRequestException(String tokenType, String token, String message) {
        super(String.format("[%s] %s / data : %s", tokenType, message, token));
    }
}
