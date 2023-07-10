package com.moa.moa3.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // 향후 security 적용할 예정이므로
                // 모든 header 를 읽을 수 있게 하였고
                // front 에서 Authorization header를 읽을 수 있도록 추가하였습니다.
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:3000")
                        .allowedMethods("*")
                        .allowedHeaders("*")
                        .allowCredentials(true)
                        .exposedHeaders("Authorization")
                        .maxAge(3600L);
            }
        };
    }
}
