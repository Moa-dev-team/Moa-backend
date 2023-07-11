package com.moa.moa3.util.oauth;

import com.moa.moa3.dto.oauth.OAuthProvider;
import com.moa.moa3.exception.oauth.NotFoundProviderException;
import jakarta.annotation.PostConstruct;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
// 생성자를 private 으로 만들어서 static method 만을 사용하게 하고 싶은데
// 의존성을 주입받아야 해서 생성자를 public 으로 만들었습니다.
// 이를 해결할 수 있는 방법이 있다면 수정하겠습니다.
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
    }
}
