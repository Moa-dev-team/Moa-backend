package com.moa2.service.auth;

import com.moa2.dto.auth.request.LoginDto;
import com.moa2.exception.jwt.InvalidTokenRequestException;
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
        jwtValidator.validateAccessToken(accessToken);

        String refreshToken = accessTokenService.getAccessToken(accessToken);
        accessTokenService.deleteAccessToken(accessToken);
        refreshTokenService.deleteRefreshToken(refreshToken);
    }

    public TokenDto refresh(String refreshToken) {
        jwtValidator.validateRefreshToken(refreshToken);

        String accessToken = refreshTokenService.getRefreshToken(refreshToken);
        accessTokenService.deleteAccessToken(accessToken);
        refreshTokenService.deleteRefreshToken(refreshToken);

        Authentication authentication = jwtTokenProvider.getAuthentication(refreshToken);
        return jwtTokenProvider.createTokens(authentication);
    }


    public Long getMemberIdInAccessTokenInBearer(String accessTokenInBearer) {
        String accessToken = resolveToken(accessTokenInBearer);
        return getMemberIdInAccessToken(accessToken);
    }

    public Long getMemberIdInAccessToken(String accessToken) {
        jwtValidator.validateAccessToken(accessToken);
        return jwtTokenProvider.getClaims(accessToken).get("memberId", Long.class);
    }



    public Long getExpirationTimeInMilliSeconds(String jwt) {
        jwtValidator.validateToken(jwt);
        return jwtTokenProvider.getClaims(jwt).getExpiration().getTime();
    }

    private String resolveToken(String accessTokenInHeader) {
        if (accessTokenInHeader != null && accessTokenInHeader.startsWith("Bearer ")) {
            return accessTokenInHeader.substring(7);
        } else {
            throw new InvalidTokenRequestException("JWT", accessTokenInHeader, "Invalid Bearer Format.");
        }
    }
}
