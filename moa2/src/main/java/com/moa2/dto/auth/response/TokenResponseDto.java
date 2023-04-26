package com.moa2.dto.auth.response;

import lombok.Data;

@Data
public class TokenResponseDto {
    String accessToken;
    Long memberId;
    Long expirationTimeInMilliSeconds;

    public TokenResponseDto(String accessToken, Long memberId, Long expirationTimeInMilliSeconds) {
        this.accessToken = accessToken;
        this.memberId = memberId;
        this.expirationTimeInMilliSeconds = expirationTimeInMilliSeconds;
    }
}
