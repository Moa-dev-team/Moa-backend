package com.moa.moa3.exception.oauth;

public class NotFoundOAuthIdException extends RuntimeException {
    public NotFoundOAuthIdException() {
        super("oauthId 가 존재하지 않습니다.");
    }
}
