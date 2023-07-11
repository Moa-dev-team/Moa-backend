package com.moa.moa3.service.oauth;

import com.moa.moa3.dto.oauth.OAuthAccessTokenResponse;
import com.moa.moa3.dto.oauth.OAuthProvider;
import com.moa.moa3.dto.oauth.UserData;
import com.moa.moa3.util.oauth.OAuthProviderFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class OAuthService {

    private final OAuthProviderFactory oAuthProviderFactory;

    public UserData getUserData(String provider, String code) {
        OAuthProvider oAuthProvider = oAuthProviderFactory.getProvider(provider);
        return new UserData("lorem", "ipsum", "dolor");
    }

    private OAuthAccessTokenResponse getAccessToken(String provider, String code) {
        return WebClient.create()
    }
}
