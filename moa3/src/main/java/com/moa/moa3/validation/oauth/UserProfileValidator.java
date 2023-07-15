package com.moa.moa3.validation.oauth;

import com.moa.moa3.dto.oauth.UserProfile;

public class UserProfileValidator {

    private UserProfileValidator() {}

    public static void validate(UserProfile userProfile) throws IllegalArgumentException {
        String oAuthId = userProfile.getOAuthId();
        String email = userProfile.getEmail();
        if (oAuthId == null || oAuthId.isEmpty() || oAuthId.isBlank()) {
            throw new IllegalArgumentException("oAuthId 가 적절하지 않습니다.");
        }
        if (email == null || email.isEmpty() || email.isBlank()) {
            throw new IllegalArgumentException("email 이 적절하지 않습니다.");
        }
    }
}

