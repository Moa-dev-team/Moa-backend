package com.moa2.dto.auth.response;

import lombok.Data;

@Data
public class SuccessLoginResponseDto {
    String accessToken;
    Long memberId;
    Long accessTokenExpirationInMilliSeconds;
    
    boolean isFirstLogin = false;

    String imageUrl;

    public SuccessLoginResponseDto(String accessToken, Long memberId, Long accessTokenExpirationInMilliSeconds) {
        this.accessToken = accessToken;
        this.memberId = memberId;
        this.accessTokenExpirationInMilliSeconds = accessTokenExpirationInMilliSeconds;
    }

    public static SuccessLoginResponseDto CreateAboutFirstLogin(String accessToken,
                                                               Long memberId,
                                                               Long accessTokenExpirationInMilliSeconds,
                                                               String imageUrl) {
        SuccessLoginResponseDto dto = new SuccessLoginResponseDto(accessToken, memberId, accessTokenExpirationInMilliSeconds);
        dto.setFirstLogin(true);
        dto.setImageUrl(imageUrl);
        return dto;
    }
}
