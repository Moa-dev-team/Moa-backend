package com.moa2.util.oauth2;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Oauth2Provider {
    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;
    private final String tokenUri;
    private final String userInfoUri;

    public Oauth2Provider(Oauth2Properties.User user, Oauth2Properties.Provider provider) {
        this(user.getClientId(), user.getClientSecret(), user.getRedirectUri(), provider.getTokenUri(), provider.getUseInfoUri());
    }

    @Builder
    public Oauth2Provider(String clientId, String clientSecret, String redirectUri, String tokenUri, String userInfoUri) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.tokenUri = tokenUri;
        this.userInfoUri = userInfoUri;
    }
}
