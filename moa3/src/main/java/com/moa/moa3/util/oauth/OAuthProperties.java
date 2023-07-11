package com.moa.moa3.util.oauth;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

// OAuthProvider 을 생성하는데만 사용합니다.
// yml 에 있는 데이터를 가져오는 역할을 합니다.
@Configuration
@ConfigurationProperties(prefix = "oauth2")
@Getter @Setter
public class OAuthProperties {
    public Github github;
    @Getter @Setter
    public static class Github {
        private String clientId;
        private String clientSecret;
        private String redirectUri;
        private String tokenUri;
        private String userInfoUri;

    }
}



