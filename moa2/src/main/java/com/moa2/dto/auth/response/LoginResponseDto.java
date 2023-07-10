package com.moa2.dto.auth.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

@Data
public class LoginResponseDto {
    private String name;
    private String imageUrl;
    private boolean isRegistered;
    private String accessToken;
    private Long refreshTokenExpirationInMilliSeconds;
    @JsonIgnore
    private String refreshToken;

    @Builder
    public LoginResponseDto(String name, String imageUrl, boolean isRegistered, String accessToken, Long refreshTokenExpirationInMilliSeconds,
                            String refreshToken) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.isRegistered = isRegistered;
        this.accessToken = accessToken;
        this.refreshTokenExpirationInMilliSeconds = refreshTokenExpirationInMilliSeconds;
        this.refreshToken = refreshToken;
    }
}
