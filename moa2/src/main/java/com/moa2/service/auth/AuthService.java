package com.moa2.service.auth;

import com.moa2.dto.auth.LoginDto;
import com.moa2.security.jwt.JwtTokenProvider;
import com.moa2.security.jwt.JwtValidator;
import com.moa2.dto.auth.TokenDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final RefreshTokenService refreshTokenService;
    private final AccessTokenService accessTokenService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final JwtValidator jwtValidator;

    public TokenDto login(@Valid LoginDto loginDto) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());

        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtTokenProvider.createTokens(authentication);
    }

    public void logout(String accessTokenInHeader) {
        String accessToken = resolveToken(accessTokenInHeader);
        if (!jwtValidator.validateAccessToken(accessToken)) {
            throw new IllegalArgumentException("Invalid token");
        }
        String refreshToken = accessTokenService.getAccessToken(accessToken);
        accessTokenService.deleteAccessToken(accessToken);
        refreshTokenService.deleteRefreshToken(refreshToken);
    }

    public TokenDto refresh(String refreshToken) {
        if (!jwtValidator.validateRefreshToken(refreshToken)) {
            throw new IllegalArgumentException("Invalid token");
        }

        Authentication authentication = jwtTokenProvider.getAuthentication(refreshToken);

        String accessToken = refreshTokenService.getRefreshToken(refreshToken);
        accessTokenService.deleteAccessToken(accessToken);
        refreshTokenService.deleteRefreshToken(refreshToken);
        return jwtTokenProvider.createTokens(authentication);
    }


    public Long getMemberId(String accessTokenInHeader) {
        if (!jwtValidator.validateAccessToken(resolveToken(accessTokenInHeader))) {
            throw new IllegalArgumentException("Invalid token");
        }
        String accessToken = resolveToken(accessTokenInHeader);
        Long memberId = jwtTokenProvider.getClaims(accessToken).get("memberId", Long.class);
        return memberId;
    }

    private String resolveToken(String accessTokenInHeader) {
        if (accessTokenInHeader != null && accessTokenInHeader.startsWith("Bearer ")) {
            return accessTokenInHeader.substring(7);
        } else {
            throw new IllegalArgumentException("Invalid token");
        }
    }
}
