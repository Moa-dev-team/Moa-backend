package com.moa.moa3.jwt;

import com.moa.moa3.dto.jwt.AtRt;
import com.moa.moa3.entity.member.Member;
import com.moa.moa3.repository.member.MemberRepository;
import com.moa.moa3.security.MemberDetails;
import com.moa.moa3.service.redis.AccessTokenService;
import com.moa.moa3.service.redis.RefreshTokenService;
import com.moa.moa3.validation.jwt.JwtTokenValidator;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Collection;

@Component
@RequiredArgsConstructor
public class JwtTokenService {
    @Value("${jwt.secret}")
    private String secretKey;
    private Key key;

    private final MemberRepository memberRepository;
    private final JwtTokenValidator jwtTokenValidator;
    private final JwtTokenProvider jwtTokenProvider;
    private final AccessTokenService accessTokenService;
    private final RefreshTokenService refreshTokenService;

    @PostConstruct
    protected void init() {
        key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    private Claims getClaims(String token) {
        jwtTokenValidator.validateToken(token);
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Authentication createAuthentication(String token) {
        Claims claims = getClaims(token);

        Collection<? extends GrantedAuthority> authorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList(claims.get("authorities").toString());
        Long memberId = claims.get("memberId", Long.class);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));
        MemberDetails memberDetails = new MemberDetails(member);

        return new UsernamePasswordAuthenticationToken(memberDetails, token, authorities);
    }

    private AtRt createAtRt(Authentication authentication) {
        String accessToken = jwtTokenProvider.createAccessToken(authentication);
        String refreshToken = jwtTokenProvider.createRefreshToken(authentication);

        accessTokenService.mapAtToRt(accessToken, refreshToken);
        refreshTokenService.mapRtToAt(refreshToken, accessToken);
        return new AtRt(accessToken, refreshToken);
    }

    public AtRt createAtRt(Member member) {
        MemberDetails memberDetails = new MemberDetails(member);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        memberDetails, null, memberDetails.getAuthorities());
        return createAtRt(authentication);
    }

    public AtRt refresh(String refreshToken) {
        jwtTokenValidator.validateRefreshToken(refreshToken);
        Authentication authentication = createAuthentication(refreshToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String oldAccessToken = refreshTokenService.getAt(refreshToken);
        accessTokenService.deleteAt(oldAccessToken);
        refreshTokenService.deleteRt(refreshToken);

        return createAtRt(authentication);
    }
    

    public Authentication createAuthenticationWithAt(String accessToken) {
        jwtTokenValidator.validateAccessToken(accessToken);
        return createAuthentication(accessToken);
    }

    public Long getTokenExpirationInMilliseconds(String token) {
        return getClaims(token).getExpiration().getTime();
    }
}
