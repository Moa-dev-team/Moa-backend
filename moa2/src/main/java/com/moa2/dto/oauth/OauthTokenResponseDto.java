package com.moa2.dto.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OauthTokenResponseDto {
    @JsonProperty("access_token")
    private String accessToken;
    private String scope;
    @JsonProperty("token_type")
    private String tokenType;

    @Builder
    public OauthTokenResponseDto(String accessToken, String scope, String tokenType) {
        this.accessToken = accessToken;
        this.scope = scope;
        this.tokenType = tokenType;
    }
}
