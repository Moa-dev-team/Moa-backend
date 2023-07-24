package com.moa.moa3.jwt;

import com.moa.moa3.dto.jwt.AtRt;
import com.moa.moa3.security.MemberDetails;
import com.moa.moa3.service.redis.AccessTokenService;
import com.moa.moa3.service.redis.RefreshTokenService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.access-token-validity-in-seconds}")
    private Long accessTokenValidityInSeconds;
    @Value("${jwt.refresh-token-validity-in-seconds}")
    private Long refreshTokenValidityInSeconds;
    private Key key;

    private final AccessTokenService accessTokenService;
    private final RefreshTokenService refreshTokenService;

    @PostConstruct
    protected void init() {
        key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    private String createAccessToken(Authentication authentication) {
        MemberDetails memberDetails = (MemberDetails) authentication.getPrincipal();

        Date validity = getDateAfter(this.accessTokenValidityInSeconds);

        return createToken(memberDetails, validity);
    }

    private String createRefreshToken(Authentication authentication) {
        MemberDetails memberDetails = (MemberDetails) authentication.getPrincipal();

        Date validity = getDateAfter(this.refreshTokenValidityInSeconds);

        return createToken(memberDetails, validity);
    }

    public AtRt createAtRt(Authentication authentication) {
        String accessToken = createAccessToken(authentication);
        String refreshToken = createRefreshToken(authentication);

        accessTokenService.mapAtToRt(accessToken, refreshToken);
        refreshTokenService.mapRtToAt(refreshToken, accessToken);
        return new AtRt(accessToken, refreshToken);
    }

    private Date getDateAfter(long seconds) {
        return new Date(new Date().getTime() + seconds * 1000);
    }

    private String createToken(MemberDetails memberDetails, Date validity) {
        return Jwts.builder()
                .setSubject(memberDetails.getUsername())
                .claim("memberId", memberDetails.getMemberId())
                .claim("authorities", memberDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(",")))
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }

}
