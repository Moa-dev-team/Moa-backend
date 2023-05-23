package com.moa2.util.oauth2;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@Getter
@ConfigurationProperties(prefix = "oauth2")
public class Oauth2Properties {
    private final Map<String, User> users = new HashMap<>();
    private final Map<String, Provider> providers = new HashMap<>();

    @Getter @Setter
    public static class User {
        private String clientId;
        private String clientSecret;
        private String redirectUri;
    }

    @Getter @Setter
    public static class Provider {
        private String tokenUri;
        private String useInfoUri;
        private String userNameAttribute;
    }

}
