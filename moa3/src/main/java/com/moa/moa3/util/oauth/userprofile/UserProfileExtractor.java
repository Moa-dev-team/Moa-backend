package com.moa.moa3.util.oauth.userprofile;

import com.moa.moa3.dto.oauth.UserProfile;

import java.util.Arrays;
import java.util.Map;

public enum UserProfileExtractor {

    GITHUB("github") {
        @Override
        public UserProfile of(Map<String, Object> attributes) {
            return UserProfile.builder()
                    .oAuthId(String.valueOf(attributes.get("id")))
                    .email(String.valueOf(attributes.get("email")))
                    .name(String.valueOf(attributes.get("name")))
                    .imageUrl(String.valueOf(attributes.get("avatar_url")))
                    .build();
        }
    };

    public static UserProfile extract(String providerName, Map<String, Object> attributes) {
        return Arrays.stream(values())
                .filter(provider -> providerName.equals(provider.providerName))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new)
                .of(attributes);
    }
    private final String providerName;
    UserProfileExtractor(String providerName) {
        this.providerName = providerName;
    }
    public abstract UserProfile of(Map<String, Object> attributes);
}