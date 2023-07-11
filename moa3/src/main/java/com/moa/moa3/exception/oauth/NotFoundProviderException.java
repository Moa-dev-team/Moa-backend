package com.moa.moa3.exception.oauth;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NotFoundProviderException extends RuntimeException{
    public NotFoundProviderException(String providerName) {
        super("NotFoundProvider: " + providerName);
        log.error("NotFoundProvider: " + providerName);
    }
}
