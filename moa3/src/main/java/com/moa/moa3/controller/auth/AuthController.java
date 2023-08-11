package com.moa.moa3.controller.auth;

import com.moa.moa3.dto.auth.LoginResponse;
import com.moa.moa3.dto.auth.RefreshResponse;
import com.moa.moa3.dto.jwt.AtRtSuccess;
import com.moa.moa3.dto.oauth.*;
import com.moa.moa3.service.oauth.OAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

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
                .maxAge(refreshTokenExpirationFromNowInSeconds)
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
        String newRefreshToken = atRtSuccess.getRefreshToken();
        Long newRefreshTokenExpirationInMilliseconds = atRtSuccess.getRefreshTokenExpirationInMilliseconds();
        Long newRefreshTokenExpirationFromNowInSeconds = atRtSuccess.getRefreshTokenExpirationFromNowInSeconds();

        HttpCookie cookie = ResponseCookie.from("refreshToken", newRefreshToken)
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

    /**
     * at 가 포함되지 않은 경우 아무일도 하지 않습니다.<br>
     * rt 가 포함되지 않은 경우 "refreshToken" 이라는 cookie 를 만료시키고 아무일도 하지 않습니다.
     * @param accessTokenInHeader
     * @param refreshToken
     * @return
     */
    @GetMapping("logout")
    public ResponseEntity oauthLogout(@RequestHeader("Authorization") String accessTokenInHeader,
                                      @CookieValue String refreshToken) {
        String accessToken = getAccessToken(accessTokenInHeader);
        oauthService.logout(accessToken, refreshToken);

        HttpCookie cookie = ResponseCookie.from("refreshToken", "")
                .maxAge(0)
                .path("/")
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("logout success");
    }

    private String getAccessToken(String accessTokenInHeader) {
        if (accessTokenInHeader.length() < BEARER_PREFIX.length())
            return "";
        return accessTokenInHeader.substring(BEARER_PREFIX.length());
    }
}
