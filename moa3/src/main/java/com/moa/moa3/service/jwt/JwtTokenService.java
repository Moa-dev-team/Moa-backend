package com.moa.moa3.service.jwt;

import com.moa.moa3.dto.jwt.AtRtSuccess;
import com.moa.moa3.entity.member.Member;
import com.moa.moa3.jwt.AuthenticationService;
import com.moa.moa3.jwt.JwtTokenProvider;
import com.moa.moa3.security.MemberDetails;
import com.moa.moa3.service.member.MemberService;
import com.moa.moa3.service.redis.AccessTokenService;
import com.moa.moa3.service.redis.RefreshTokenService;
import com.moa.moa3.validation.jwt.JwtTokenValidator;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class JwtTokenService {
    private final JwtTokenProvider jwtTokenProvider;
    private final AccessTokenService accessTokenService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationService authenticationService;



    private AtRtSuccess createAtRt(Authentication authentication) {
        String accessToken = jwtTokenProvider.createAccessToken(authentication);
        String refreshToken = jwtTokenProvider.createRefreshToken(authentication);
        Long refreshTokenExpirationInMilliseconds = getTokenExpirationInMilliseconds(refreshToken);

        accessTokenService.mapAtToRt(accessToken, refreshToken);
        refreshTokenService.mapRtToAt(refreshToken, accessToken);
        return new AtRtSuccess(accessToken, refreshToken, refreshTokenExpirationInMilliseconds);
    }

    public AtRtSuccess createAtRt(Member member) {
        MemberDetails memberDetails = new MemberDetails(member);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        memberDetails, null, memberDetails.getAuthorities());
        return createAtRt(authentication);
    }

    public AtRtSuccess refresh(String refreshToken) {
        Authentication authentication =
                authenticationService.createAuthenticationWithRt(refreshToken);

        String oldAccessToken = refreshTokenService.getAt(refreshToken);
        accessTokenService.deleteAt(oldAccessToken);
        refreshTokenService.deleteRt(refreshToken);

        return createAtRt(authentication);
    }

    private Long getTokenExpirationInMilliseconds(String token) {
        Claims claims = jwtTokenProvider.getClaims(token);
        return claims.getExpiration().getTime();
    }
}
