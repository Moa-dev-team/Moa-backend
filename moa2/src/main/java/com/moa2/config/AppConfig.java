package com.moa2.config;

import com.moa2.util.oauth2.Oauth2Properties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public Oauth2Properties oauth2Properties() {
        return new Oauth2Properties();
    }
}
