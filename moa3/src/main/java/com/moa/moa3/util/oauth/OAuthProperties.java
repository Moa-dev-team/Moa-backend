package com.moa.moa3.util.oauth;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "oauth2")
@Getter @Setter
public class OAuthProperties {
    private User user;
    private Provider provider;

    @Getter @Setter
    public static class User {
        private Github github;

        @Getter @Setter
        public static class Github {
            private String clientId;
            private String clientSecret;
            private String redirectUri;
        }
    }

    @Getter @Setter
    public static class Provider {
        private Github github;

        @Getter @Setter
        public static class Github {
            private String tokenUri;
            private String userInfoUri;
        }
    }

}
