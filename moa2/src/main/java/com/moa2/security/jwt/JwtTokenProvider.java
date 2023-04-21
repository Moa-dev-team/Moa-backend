package com.moa2.security.jwt;

import com.moa2.dto.auth.TokenDto;
import com.moa2.repository.member.MemberRepository;
import com.moa2.service.auth.AccessTokenService;
import com.moa2.service.auth.RefreshTokenService;
import com.moa2.security.userdetails.MemberDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtTokenProvider implements InitializingBean {
    private final String secret;
    private final long tokenValidTimeInMilliseconds;
    private final long refreshTokenValidTimeInMilliseconds;
    private Key key;

    private final RefreshTokenService refreshTokenService;
    private final AccessTokenService accessTokenService;

    private final MemberRepository memberRepository;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret, // hmac 암호화를 사용하므로 32bit 를 넘어야한다.
            @Value("${jwt.access-token-validity-in-seconds}") long tokenValidTime,
            @Value("${jwt.refresh-token-validity-in-seconds}") long refreshTokenValidTime,
            RefreshTokenService refreshTokenService,
            AccessTokenService accessTokenService,
            MemberRepository memberRepository) {
        this.secret = secret;
        this.tokenValidTimeInMilliseconds = tokenValidTime * 1000;
        this.refreshTokenValidTimeInMilliseconds = refreshTokenValidTime * 1000;
        this.refreshTokenService = refreshTokenService;
        this.accessTokenService = accessTokenService;
        this.memberRepository = memberRepository;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String createToken(Authentication authentication, boolean rememberMe) {
        MemberDetails memberDetails = (MemberDetails) authentication.getPrincipal();

        long now = (new Date()).getTime();
        Date validity;

        if (rememberMe) {
            validity = new Date(now + this.refreshTokenValidTimeInMilliseconds);
        } else {
            validity = new Date(now + this.tokenValidTimeInMilliseconds);
        }

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim("memberId", memberDetails.getMemberId())
                .claim("authorities", memberDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(",")))
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }

    public TokenDto createTokens(Authentication authentication) {
        String accessToken = createToken(authentication, false);
        String refreshToken = createToken(authentication, true);
        refreshTokenService.setRefreshTokenWithAccessToken(refreshToken, accessToken);
        accessTokenService.setAccessTokenWithRefreshToken(accessToken, refreshToken);
        return new TokenDto(accessToken, refreshToken);
    }


    @Transactional
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);

        Collection<? extends GrantedAuthority> authorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList(claims.get("authorities").toString());

        Long memberId = claims.get("memberId", Long.class);

        MemberDetails principal = new MemberDetails(memberRepository.findById(memberId)
                .orElseThrow(
                        () -> new IllegalArgumentException("there is no user. id=" + memberId)
                )
        );

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }


    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    // for test
    public String createTokenWithSeconds(Authentication authentication, boolean rememberMe, Long seconds) {
        MemberDetails memberDetails = (MemberDetails) authentication.getPrincipal();

        long now = (new Date()).getTime();
        Date validity = new Date(now + seconds * 1000);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim("memberId", memberDetails.getMemberId())
                .claim("authorities", memberDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(",")))
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }
}