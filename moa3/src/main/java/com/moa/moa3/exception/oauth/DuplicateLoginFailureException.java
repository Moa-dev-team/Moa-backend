package com.moa.moa3.exception.oauth;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DuplicateLoginFailureException extends RuntimeException{
    public DuplicateLoginFailureException(String providerName) {
        super("이메일 중복: 이미 [" + providerName + "] 으로 가입된 계정이 존재합니다.");
        log.error("이메일 중복: 이미 [" + providerName + "] 으로 가입된 계정이 존재합니다.");
    }
}
