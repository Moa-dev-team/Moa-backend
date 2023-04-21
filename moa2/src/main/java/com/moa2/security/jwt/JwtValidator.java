package com.moa2.security.jwt;

import com.moa2.exception.jwt.InvalidTokenRequestException;
import com.moa2.service.auth.AccessTokenService;
import com.moa2.service.auth.RefreshTokenService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtValidator {
    private final JwtTokenProvider jwtTokenProvider;
    private final AccessTokenService accessTokenService;
    private final RefreshTokenService refreshTokenService;

    public boolean validateToken(String authToken) {
        try {
            jwtTokenProvider.getClaims(authToken);
        } catch (SignatureException e) {
            log.error("Invalid JWT signature.");
            throw new InvalidTokenRequestException("JWT", authToken, "Invalid JWT signature.");
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token.");
            throw new InvalidTokenRequestException("JWT", authToken, "Invalid JWT token.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token.");
            throw new InvalidTokenRequestException("JWT", authToken, "Expired JWT token.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token.");
            throw new InvalidTokenRequestException("JWT", authToken, "Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty.");
            throw new InvalidTokenRequestException("JWT", authToken, "JWT claims string is empty.");
        }
        return true;
    }

    public boolean validateRefreshToken(String refreshToken) {
        validateToken(refreshToken);
        if (!refreshTokenService.isExistRefreshToken(refreshToken)) {
            throw new InvalidTokenRequestException("JWT", refreshToken, "Invalid refresh token.");
        }
        return true;
    }

    public boolean validateAccessToken(String accessToken) {
        validateToken(accessToken);
        if (!accessTokenService.isExistAccessToken(accessToken)) {
            throw new InvalidTokenRequestException("JWT", accessToken, "Invalid access token.");
        }
        return true;
    }
}