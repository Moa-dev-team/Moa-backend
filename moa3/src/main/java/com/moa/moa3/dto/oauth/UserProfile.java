package com.moa.moa3.dto.oauth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserProfile {
    private String oauthId;
    private String name;
    private String email;
    private String imageUrl;

    @Builder
    public UserProfile(String oauthId, String name, String email, String imageUrl) {
        this.oauthId = oauthId;
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
    }
}
