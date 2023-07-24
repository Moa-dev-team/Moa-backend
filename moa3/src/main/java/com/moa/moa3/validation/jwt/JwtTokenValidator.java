package com.moa.moa3.validation.jwt;

import com.moa.moa3.exception.jwt.InvalidTokenRequestException;
import com.moa.moa3.jwt.JwtTokenService;
import com.moa.moa3.repository.member.MemberRepository;
import com.moa.moa3.service.redis.AccessTokenService;
import com.moa.moa3.service.redis.RefreshTokenService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenValidator {
    @Value("${jwt.secret}")
    private String secretKey;
    private Key key;

    @PostConstruct
    protected void init() {
        key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }
    private final AccessTokenService accessTokenService;
    private final RefreshTokenService refreshTokenService;

    public boolean validateToken(String jwtToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwtToken);
        } catch (SignatureException e) {
            log.error("Invalid JWT signature.");
            throw new InvalidTokenRequestException("JWT", jwtToken, "Invalid JWT signature.");
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token.");
            throw new InvalidTokenRequestException("JWT", jwtToken, "Invalid JWT token.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token.");
            throw new InvalidTokenRequestException("JWT", jwtToken, "Expired JWT token.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token.");
            throw new InvalidTokenRequestException("JWT", jwtToken, "Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty.");
            throw new InvalidTokenRequestException("JWT", jwtToken, "JWT claims string is empty.");
        }
        return true;
    }

    public boolean validateRefreshToken(String refreshToken) {
        validateToken(refreshToken);
        if (!refreshTokenService.isExist(refreshToken)) {
            throw new InvalidTokenRequestException("refresh token", refreshToken, "expired or deleted.");
        }
        return true;
    }

    public boolean validateAccessToken(String accessToken) {
        validateToken(accessToken);
        if (!accessTokenService.isExist(accessToken)) {
            throw new InvalidTokenRequestException("access token", accessToken, "expired or deleted.");
        }
        return true;
    }

}
