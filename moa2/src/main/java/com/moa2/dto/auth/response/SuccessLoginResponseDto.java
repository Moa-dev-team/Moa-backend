package com.moa2.dto.auth.response;

import lombok.Data;

@Data
public class SuccessLoginResponseDto {
    String accessToken;
    Long refreshTokenExpirationInMilliSeconds;
    boolean isFirstLogin = false;

    SuccessLoginResponseDto() {
    }

    public SuccessLoginResponseDto(String accessToken, Long refreshTokenExpirationInMilliSeconds) {
        this.accessToken = accessToken;
        this.refreshTokenExpirationInMilliSeconds = refreshTokenExpirationInMilliSeconds;
    }
}
