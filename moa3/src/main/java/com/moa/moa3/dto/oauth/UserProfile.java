package com.moa.moa3.dto.oauth;

import lombok.Builder;
import lombok.Data;

@Data
public class UserProfile {
    private String name;
    private String email;
    private String imageUrl;

    @Builder
    public UserProfile(String name, String email, String imageUrl) {
        this.name = name;

        if (email == null || email.isEmpty() || email.isBlank()) {
            throw new IllegalArgumentException("적절하지 않은 oAuthId 입니다.");
        } else {
            this.email = email;
        }

        this.imageUrl = imageUrl;
    }
}
