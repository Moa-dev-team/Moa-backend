package com.moa.moa3.controller.auth;

import com.moa.moa3.dto.auth.LoginResponse;
import com.moa.moa3.dto.auth.RefreshResponse;
import com.moa.moa3.dto.jwt.AtRt;
import com.moa.moa3.dto.oauth.*;
import com.moa.moa3.jwt.JwtTokenService;
import com.moa.moa3.service.oauth.OAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final OAuthService oauthService;
    private final JwtTokenService jwtTokenService;

    @GetMapping("login/{provider}")
    public ResponseEntity oauthLogin(@PathVariable String provider, @RequestParam String code) {
        LoginSuccess loginSuccess = oauthService.login(provider, code);

        Long refreshTokenExpirationInMilliseconds = jwtTokenService.getTokenExpirationInMilliseconds(loginSuccess.getRefreshToken());
        Long refreshTokenExpirationFromNowInSeconds = calTokenExpirationFromNowInSeconds(refreshTokenExpirationInMilliseconds);

        HttpCookie cookie = ResponseCookie.from("refreshToken", loginSuccess.getRefreshToken())
                .path("/")
                .maxAge(refreshTokenExpirationFromNowInSeconds)
//                .secure(true)
                .httpOnly(true)
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new LoginResponse(loginSuccess, refreshTokenExpirationInMilliseconds));
    }

    @GetMapping("refresh")
    public ResponseEntity oauthRefresh(@CookieValue String refreshToken) {
        AtRt atRt = oauthService.refresh(refreshToken);

        String newAccessToken = atRt.getAccessToken();
        String newRefreshToken = atRt.getRefreshToken();

        Long newRefreshTokenExpirationInMilliseconds = jwtTokenService.getTokenExpirationInMilliseconds(newRefreshToken);
        Long newRefreshTokenExpirationFromNowInSeconds = calTokenExpirationFromNowInSeconds(newRefreshTokenExpirationInMilliseconds);

        HttpCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .path("/")
                .maxAge(newRefreshTokenExpirationFromNowInSeconds)
//                .secure(true)
                .httpOnly(true)
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new RefreshResponse(newAccessToken, newRefreshTokenExpirationInMilliseconds));
    }

    private Long calTokenExpirationFromNowInSeconds(Long tokenExpirationInMilliSeconds) {
        // refreshToken 이 만료하는 시간보다 10분 일찍 쿠키 수명이 끝나도록 설정
        long t =  (tokenExpirationInMilliSeconds - (new Date().getTime())) / 1000;
        return Math.max(t - 600, 0);
    }
}
