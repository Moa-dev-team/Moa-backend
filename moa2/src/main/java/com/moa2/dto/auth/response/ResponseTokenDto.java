package com.moa2.dto.auth.response;

import lombok.Data;

@Data
public class ResponseTokenDto {
    String accessToken;
    Long memberId;
    Long accessTokenExpirationInMilliSeconds;

    public ResponseTokenDto(String accessToken, Long memberId, Long accessTokenExpirationInMilliSeconds) {
        this.accessToken = accessToken;
        this.memberId = memberId;
        this.accessTokenExpirationInMilliSeconds = accessTokenExpirationInMilliSeconds;
    }
}
