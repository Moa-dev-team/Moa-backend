package com.moa.moa3.dto.auth;

import lombok.Data;

@Data
public class RefreshResponse {
    private String accessToken;
    private Long refreshTokenExpirationInMilliSeconds;

    public RefreshResponse(String accessToken, Long refreshTokenExpirationInMilliSeconds) {
        this.accessToken = accessToken;
        this.refreshTokenExpirationInMilliSeconds = refreshTokenExpirationInMilliSeconds;
    }
}
