package com.moa2.config;

import com.moa2.security.jwt.JwtFilter;
import com.moa2.security.jwt.exception.JwtAccessDeniedHandler;
import com.moa2.security.jwt.exception.JwtAuthenticationEntryPoint;
import com.moa2.security.oauth2.CustomOAuth2UserService;
import com.moa2.security.oauth2.OAuth2AuthenticationFailureHandler;
import com.moa2.security.oauth2.OAuth2AuthenticationSuccessHandler;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class    SecurityConfig {
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtFilter jwtFilter;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.cors().configurationSource(new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                config.setAllowedMethods(Collections.singletonList("*"));
                config.setAllowedHeaders(Collections.singletonList("*"));
                config.setAllowCredentials(true);
                config.setExposedHeaders(List.of("Authorization")); // 사용할 header 이므로 추가
                config.setMaxAge(3600L);
                return config;
            }
        });


        http
            .csrf().disable()

            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                    .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                    .accessDeniedHandler(jwtAccessDeniedHandler)

            // create no session
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

            .and()
            .authorizeHttpRequests()
                .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                .requestMatchers("/user/**").hasAuthority("ROLE_USER")
                .requestMatchers("/auth/**", "/oauth/**").permitAll()
                .requestMatchers("/",
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
                        "/**/*.js").permitAll()
                .requestMatchers( "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .anyRequest().authenticated()

            .and()
//                .oauth2Client()
//            .oauth2Login()
//                .authorizationEndpoint()
//                    .baseUri("/oauth2/authorize")
//                    .and()
//                .redirectionEndpoint()
//                    .baseUri("/oauth2/callback/*")
//                    .and()
//            .userInfoEndpoint()
//                .userService(customOAuth2UserService)
//                .and()
//            .successHandler(oAuth2AuthenticationSuccessHandler)
//            .failureHandler(oAuth2AuthenticationFailureHandler)
            ;


        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
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
