package com.moa.moa3.dto.jwt;

import lombok.Data;

@Data
public class AtRt {
    String accessToken;
    String refreshToken;

    public AtRt(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
