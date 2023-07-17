package com.moa.moa3.service.oauth;

import com.moa.moa3.api.oauth.OAuthApi;
import com.moa.moa3.dto.oauth.OAuthAccessTokenResponse;
import com.moa.moa3.dto.oauth.OAuthProvider;
import com.moa.moa3.dto.oauth.UserProfile;
import com.moa.moa3.util.oauth.OAuthProviderFactory;
import com.moa.moa3.util.oauth.userprofile.UserProfileMapperFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class OAuthService {

    private final OAuthProviderFactory oAuthProviderFactory;

    public UserProfile getUserProfile(String provider, String code) {
        OAuthProvider oAuthProvider = oAuthProviderFactory.getProvider(provider);

        OAuthAccessTokenResponse accessTokenResponse = OAuthApi.getAccessTokenResponse(code, oAuthProvider);
        Map<String, Object> userAttributes = OAuthApi.getUserAttributes(
                oAuthProvider, accessTokenResponse.getAccessToken());
        return UserProfileMapperFactory.getMapper(provider).map(userAttributes);
    }
}
