package com.moa2.dto.auth.response;

import lombok.Data;

@Data
public class SuccessLoginResponseDto {
    String accessToken;
    Long accessTokenExpirationInMilliSeconds;
    boolean isFirstLogin = false;

    SuccessLoginResponseDto() {
    }

    public SuccessLoginResponseDto(String accessToken, Long accessTokenExpirationInMilliSeconds) {
        this.accessToken = accessToken;
        this.accessTokenExpirationInMilliSeconds = accessTokenExpirationInMilliSeconds;
    }
}
