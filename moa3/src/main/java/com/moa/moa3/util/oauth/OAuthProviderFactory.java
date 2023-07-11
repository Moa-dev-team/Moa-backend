package com.moa.moa3.util.oauth;

import com.moa.moa3.dto.oauth.OAuthProvider;
import jakarta.annotation.PostConstruct;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OAuthProviderFactory {
    private static final Map<String, OAuthProvider> providers = new HashMap<>();
    private final OAuthProperties properties;

    public OAuthProvider getProvider(String name) {
        return providers.get(name);
    }

    @PostConstruct
    public void init() {
        OAuthProperties.Github github = properties.getGithub();
        providers.put("github", OAuthProvider.builder()
                .clientId(github.getClientId())
                .clientSecret(github.getClientSecret())
                .redirectUri(github.getRedirectUri())
                .tokenUri(github.getTokenUri())
                .userInfoUri(github.getUserInfoUri())
                .build());
    }

}
