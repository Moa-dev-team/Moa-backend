package com.moa.moa3.dto.oauth;

import lombok.Builder;
import lombok.Data;

@Data
public class UserProfile {
    private String oAuthId;
    private String name;
    private String email;
    private String imageUrl;

    @Builder
    public UserProfile(String oAuthId, String name, String email, String imageUrl) {
        this.oAuthId = oAuthId;
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
    }
}
