package com.moa.moa3.dto.oauth;

import com.moa.moa3.entity.member.Member;
import lombok.Builder;
import lombok.Data;

@Data
public class LoginSuccess{
    private String name;
    private String email;
    private String imageUrl;
    private boolean firstLogin;
    private String accessToken;
    private String refreshToken;

    public LoginSuccess(Member member, boolean firstLogin, String accessToken, String refreshToken){
        this.name = member.getName();
        this.email = member.getEmail();
        this.imageUrl = member.getImageUrl();
        this.firstLogin = firstLogin;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
