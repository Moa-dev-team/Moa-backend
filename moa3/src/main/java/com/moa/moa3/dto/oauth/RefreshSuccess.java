package com.moa.moa3.dto.oauth;

import lombok.Data;

@Data
public class RefreshSuccess {
    private String accessToken;
    private String refreshToken;

    public RefreshSuccess(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
