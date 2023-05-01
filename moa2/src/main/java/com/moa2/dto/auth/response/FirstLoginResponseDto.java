package com.moa2.dto.auth.response;

import lombok.Data;

@Data
public class FirstLoginResponseDto extends SuccessLoginResponseDto{
    private String imageUrl;
    private String nickname;

    public FirstLoginResponseDto(String accessToken,
                                 Long accessTokenExpirationInMilliSeconds,
                                 String imageUrl, String nickname) {
        super(accessToken, accessTokenExpirationInMilliSeconds);
        this.imageUrl = imageUrl;
        this.nickname = nickname;
        super.isFirstLogin = true;
    }
}
