package com.moa.moa3.util.oauth;

import com.moa.moa3.exception.oauth.NotFoundProviderException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuthProviderFactory {
    private static final Map<String, OAuthProvider> providers = new HashMap<>();
    private final OAuthProperties properties;

    public OAuthProvider getProvider(String providerName) {
        return Optional.ofNullable(providers.get(providerName))
                .orElseThrow(() -> new NotFoundProviderException(providerName));
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

        OAuthProperties.Google google = properties.getGoogle();
        providers.put("google", OAuthProvider.builder()
                .clientId(google.getClientId())
                .clientSecret(google.getClientSecret())
                .redirectUri(google.getRedirectUri())
                .tokenUri(google.getTokenUri())
                .userInfoUri(google.getUserInfoUri())
                .build());

        OAuthProperties.Naver naver = properties.getNaver();
        providers.put("naver", OAuthProvider.builder()
                .clientId(naver.getClientId())
                .clientSecret(naver.getClientSecret())
                .redirectUri(naver.getRedirectUri())
                .tokenUri(naver.getTokenUri())
                .userInfoUri(naver.getUserInfoUri())
                .build());
    }
}
