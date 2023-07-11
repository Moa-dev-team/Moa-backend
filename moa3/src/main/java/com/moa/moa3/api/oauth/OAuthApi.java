package com.moa.moa3.api.oauth;

import com.moa.moa3.dto.oauth.OAuthAccessTokenResponse;
import com.moa.moa3.dto.oauth.OAuthProvider;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

@Component
public class OAuthApi {
    public OAuthAccessTokenResponse getAccessTokenResponse(String code, OAuthProvider oAuthProvider) {
        return WebClient.create()
                .post()
                .uri(oAuthProvider.getTokenUri())
                .headers(header -> {
                    header.setBasicAuth(oAuthProvider.getClientId(), oAuthProvider.getClientSecret());
                    header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                    header.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
                    header.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
                })
                .bodyValue(accessTokenRequestForm(code, oAuthProvider))
                .retrieve()
                .bodyToMono(OAuthAccessTokenResponse.class)
                .block();
    }

    // getAccessToken 의 method 에서만 사용하기 위해 만든 private 함수입니다.
    private MultiValueMap<String, String> accessTokenRequestForm(String code, OAuthProvider oAuthProvider) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("code", code);
        form.add("grant_type", "authorization_code");
        form.add("redirect_uri", oAuthProvider.getRedirectUri());
        return form;
    }

    public Map<String, Object> getUserAttributes(OAuthProvider oAuthProvider, String accessToken) {
        return WebClient.create()
                .get()
                .uri(oAuthProvider.getUserInfoUri())
                .headers(header -> header.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
    }




}
