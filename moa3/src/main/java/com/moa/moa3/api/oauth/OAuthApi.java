package com.moa.moa3.api.oauth;

import com.moa.moa3.dto.oauth.OAuthAccessTokenResponse;
import com.moa.moa3.dto.oauth.OAuthProvider;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

@Component
public class OAuthApi {
    public OAuthAccessTokenResponse getAccessToken(String code, OAuthProvider provider) {
        return WebClient.create()
                .post()
                .uri(provider.getTokenUri())
                .headers(header -> {
                    header.setBasicAuth(provider.getClientId(), provider.getClientSecret());
                    header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                    header.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
                    header.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
                })
                .bodyValue(accessTokenRequestForm(code, provider))
                .retrieve()
                .bodyToMono(OAuthAccessTokenResponse.class)
                .block();
    }

    // getAccessToken 의 method 에서만 사용하기 위해 만든 private 함수입니다.
    private MultiValueMap<String, String> accessTokenRequestForm(String code, OAuthProvider provider) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("code", code);
        form.add("grant_type", "authorization_code");
        form.add("redirect_uri", provider.getRedirectUri());
        return form;
    }




}
