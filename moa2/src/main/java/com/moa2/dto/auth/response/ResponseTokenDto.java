package com.moa2.dto.auth.response;

import lombok.Data;

@Data
public class ResponseTokenDto {
    String accessToken;
    Long memberId;
    Long expirationTimeInMilliSeconds;

    public ResponseTokenDto(String accessToken, Long memberId, Long expirationTimeInMilliSeconds) {
        this.accessToken = accessToken;
        this.memberId = memberId;
        this.expirationTimeInMilliSeconds = expirationTimeInMilliSeconds;
    }
}
