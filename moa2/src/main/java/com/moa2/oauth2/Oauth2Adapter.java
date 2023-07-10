package com.moa2.oauth2;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class Oauth2Adapter {
    private Oauth2Adapter() {}

    public static Map<String, Oauth2Provider> getOauthProviders(Oauth2Properties properties) {
        Map<String, Oauth2Provider> oauthProvider = new HashMap<>();

        properties.getUser().forEach((key, value) -> oauthProvider.put(
                key, new Oauth2Provider(value, properties.getProvider().get(key))));
        return oauthProvider;
    }
}
