package com.moa.moa3.controller.auth;

import com.moa.moa3.dto.auth.LoginResponse;
import com.moa.moa3.dto.auth.RefreshResponse;
import com.moa.moa3.dto.jwt.AtRtSuccess;
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
    private final String BEARER_PREFIX = "Bearer ";

    @GetMapping("login/{provider}")
    public ResponseEntity oauthLogin(@PathVariable String provider, @RequestParam String code) {
        LoginSuccess loginSuccess = oauthService.login(provider, code);

        String accessToken = loginSuccess.getAtRtSuccess().getAccessToken();
        String refreshToken = loginSuccess.getAtRtSuccess().getRefreshToken();
        Long refreshTokenExpirationInMilliseconds =
                loginSuccess.getAtRtSuccess().getRefreshTokenExpirationInMilliseconds();
        Long refreshTokenExpirationFromNowInSeconds =
                loginSuccess.getAtRtSuccess().getRefreshTokenExpirationFromNowInSeconds();

        HttpCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .path("/")
                // 쿠키의 만료시간을 10분 일찍 끝나도록 설정하였습니다.
                .maxAge(refreshTokenExpirationFromNowInSeconds - 600)
//                .secure(true)
                .httpOnly(true)
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + accessToken)
                .body(new LoginResponse(loginSuccess, refreshTokenExpirationInMilliseconds));
    }

    @GetMapping("refresh")
    public ResponseEntity oauthRefresh(@CookieValue String refreshToken) {
        AtRtSuccess atRtSuccess = oauthService.refresh(refreshToken);

        String newAccessToken = atRtSuccess.getAccessToken();
        Long newRefreshTokenExpirationInMilliseconds = atRtSuccess.getRefreshTokenExpirationInMilliseconds();
        Long newRefreshTokenExpirationFromNowInSeconds = atRtSuccess.getRefreshTokenExpirationFromNowInSeconds();

        HttpCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .path("/")
                .maxAge(newRefreshTokenExpirationFromNowInSeconds)
//                .secure(true)
                .httpOnly(true)
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + newAccessToken)
                .body(new RefreshResponse(newRefreshTokenExpirationInMilliseconds));
    }

}
