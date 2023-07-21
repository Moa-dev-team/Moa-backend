package com.moa.moa3.dto.oauth;

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
