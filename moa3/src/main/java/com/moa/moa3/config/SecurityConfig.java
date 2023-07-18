package com.moa.moa3.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private static final String[] PUBLIC_MATCHERS = {
            "/",
            "/favicon.ico",
            "/**/*.json",
            "/**/*.xml",
            "/**/*.properties",
            "/**/*.woff2",
            "/**/*.woff",
            "/**/*.ttf",
            "/**/*.ttc",
            "/**/*.ico",
            "/**/*.bmp",
            "/**/*.png",
            "/**/*.gif",
            "/**/*.svg",
            "/**/*.jpg",
            "/**/*.jpeg",
            "/**/*.html",
            "/**/*.css",
            "/**/*.js"
    };

    private static final String[] SWAGGER_MATCHERS = {
            "/swagger-ui/**",
            "/v3/api-docs/**"
    };

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers(PUBLIC_MATCHERS).permitAll()
                .requestMatchers(SWAGGER_MATCHERS).permitAll()
                .anyRequest().authenticated();
        return http.build();
    }
}
