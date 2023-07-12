package com.moa.moa3.exception.oauth;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IllegalOAuthIdException extends RuntimeException {
    public IllegalOAuthIdException() {
        super("oauthId 가 존재하지 않습니다.");
        log.error("oauthId 가 존재하지 않습니다.");
    }
}
