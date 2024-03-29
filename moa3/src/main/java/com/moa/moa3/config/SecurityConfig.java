package com.moa.moa3.config;

import com.moa.moa3.jwt.JwtAccessDeniedHandler;
import com.moa.moa3.jwt.JwtAuthenticationEntryPoint;
import com.moa.moa3.jwt.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtFilter jwtFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

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
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);

        http.cors((cors) -> {});

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling((exceptionHandling) -> exceptionHandling
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler));

        http.sessionManagement((sessionManagement) -> sessionManagement
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authorizeHttpRequests(authorize ->
                authorize.requestMatchers("/auth/**", "/global/**", "/ws/**").permitAll()
                        .requestMatchers("/user", "/user/**").hasAuthority("ROLE_USER")
                        .requestMatchers(PUBLIC_MATCHERS).permitAll()
                        .requestMatchers(SWAGGER_MATCHERS).permitAll()
                        .anyRequest().authenticated());

        return http.build();
    }
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers( "/swagger-ui/**", "/v3/api-docs/**");
    }

}
