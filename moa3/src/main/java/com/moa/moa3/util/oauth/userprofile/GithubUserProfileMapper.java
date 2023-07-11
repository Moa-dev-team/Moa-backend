package com.moa.moa3.util.oauth.userprofile;

import com.moa.moa3.dto.oauth.UserProfile;

import java.util.Map;

public class GithubUserProfileMapper implements UserProfileMapper{
    @Override
    public UserProfile map(Map<String, Object> attributes) {
        return UserProfile.builder()
                .oauthId(String.valueOf(attributes.get("id")))
                .email(String.valueOf(attributes.get("email")))
                .name(String.valueOf(attributes.get("name")))
                .imageUrl(String.valueOf(attributes.get("avatar_url")))
                .build();
    }
}
