package com.moa.moa3.exception.oauth;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NotFoundEmailException extends RuntimeException{
    public NotFoundEmailException() {
        super("email 이 존재하지 않습니다.");
        log.error("email 이 존재하지 않습니다.");
    }
}
