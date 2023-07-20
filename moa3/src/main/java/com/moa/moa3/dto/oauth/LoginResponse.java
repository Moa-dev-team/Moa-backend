package com.moa.moa3.dto.oauth;

import lombok.Data;

@Data
public class LoginResponse {
    private String name;
    private String email;
    private String imageUrl;
    private boolean firstLogin;
    private String accessToken;
    private Long refreshTokenExpirationInMilliSeconds;

    public LoginResponse(LoginSuccess loginSuccess, Long refreshTokenExpirationInMilliSeconds){
        this.name = loginSuccess.getName();
        this.email = loginSuccess.getEmail();
        this.imageUrl = loginSuccess.getImageUrl();
        this.firstLogin = loginSuccess.isFirstLogin();
        this.accessToken = loginSuccess.getAccessToken();
        this.refreshTokenExpirationInMilliSeconds = refreshTokenExpirationInMilliSeconds;
    }
}
