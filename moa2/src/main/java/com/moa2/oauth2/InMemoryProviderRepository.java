package com.moa2.oauth2;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class InMemoryProviderRepository {
    private final Map<String, Oauth2Provider> providers;

    public InMemoryProviderRepository(Map<String, Oauth2Provider> providers) {
        this.providers = new HashMap<>(providers);
    }

    public Oauth2Provider findByProviderName(String name) {
        return providers.get(name);
    }
}
