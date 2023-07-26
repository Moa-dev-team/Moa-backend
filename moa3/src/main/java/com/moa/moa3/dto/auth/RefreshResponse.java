package com.moa.moa3.dto.auth;

import lombok.Data;

@Data
public class RefreshResponse {
    private Long refreshTokenExpirationInMilliSeconds;

    public RefreshResponse(Long refreshTokenExpirationInMilliSeconds) {
        this.refreshTokenExpirationInMilliSeconds = refreshTokenExpirationInMilliSeconds;
    }
}
