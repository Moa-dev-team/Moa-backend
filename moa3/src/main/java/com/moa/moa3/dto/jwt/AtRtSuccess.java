package com.moa.moa3.dto.jwt;

import lombok.Data;

import java.util.Date;

@Data
public class AtRtSuccess {
    String accessToken;
    String refreshToken;
    Long refreshTokenExpirationInMilliseconds;
    Long refreshTokenExpirationFromNowInSeconds;

    private Long calRefreshTokenExpirationFromNowInSeconds(Long refreshTokenExpirationInMilliseconds) {
        return (refreshTokenExpirationInMilliseconds - (new Date().getTime())) / 1000;
    }

    public AtRtSuccess(String accessToken, String refreshToken, Long refreshTokenExpirationInMilliseconds) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.refreshTokenExpirationInMilliseconds = refreshTokenExpirationInMilliseconds;
        this.refreshTokenExpirationFromNowInSeconds =
                calRefreshTokenExpirationFromNowInSeconds(refreshTokenExpirationInMilliseconds);
    }
}
