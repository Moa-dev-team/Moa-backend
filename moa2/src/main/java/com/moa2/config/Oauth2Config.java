package com.moa2.config;

import com.moa2.oauth2.InMemoryProviderRepository;
import com.moa2.oauth2.Oauth2Adapter;
import com.moa2.oauth2.Oauth2Properties;
import com.moa2.oauth2.Oauth2Provider;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@EnableConfigurationProperties(Oauth2Properties.class)
public class Oauth2Config {
    private final Oauth2Properties properties;

    public Oauth2Config(Oauth2Properties properties) {
        this.properties = properties;
    }

    @Bean
    public InMemoryProviderRepository inMemoryProviderRepository() {
        Map<String, Oauth2Provider> providers = Oauth2Adapter.getOauthProviders(properties);
        return new InMemoryProviderRepository(providers);
    }
}
