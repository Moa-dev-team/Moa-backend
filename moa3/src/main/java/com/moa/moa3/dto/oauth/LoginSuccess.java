package com.moa.moa3.dto.oauth;

import com.moa.moa3.dto.jwt.AtRtSuccess;
import com.moa.moa3.entity.member.Member;
import lombok.Data;

@Data
public class LoginSuccess{
    private String name;
    private String email;
    private String imageUrl;
    private boolean firstLogin;
    private AtRtSuccess atRtSuccess;

    public LoginSuccess(Member member, AtRtSuccess atRtSuccess){
        this.name = member.getName();
        this.email = member.getEmail();
        this.imageUrl = member.getImageUrl();
        this.firstLogin = member.isFirstLogin();
        this.atRtSuccess = atRtSuccess;
    }
}
