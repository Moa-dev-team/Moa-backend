package com.moa.moa3.dto.oauth;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class OAuthProvider {
    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String tokenUri;
    private String userInfoUri;
}
