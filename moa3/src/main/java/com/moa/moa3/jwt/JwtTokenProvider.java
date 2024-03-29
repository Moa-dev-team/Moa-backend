package com.moa.moa3.jwt;

import com.moa.moa3.security.MemberDetails;
import io.jsonwebtoken.Claims;
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


//JwtTokenProvider 는 secret key 값을 가지고 있는 객체로 이를 필요로 하는 다양한 곳에서 활용됩니다.
//따라서 순환 의존성을 가지지 않도록 JwtTokenProvider 에는 추가적인 의존성을 가지지 않는 것을 권장합니다.
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

    @PostConstruct
    protected void init() {
        key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String createAccessToken(Authentication authentication) {
        MemberDetails memberDetails = (MemberDetails) authentication.getPrincipal();

        Date validity = getDateAfter(this.accessTokenValidityInSeconds);

        return createToken(memberDetails, validity);
    }

    public String createRefreshToken(Authentication authentication) {
        MemberDetails memberDetails = (MemberDetails) authentication.getPrincipal();

        Date validity = getDateAfter(this.refreshTokenValidityInSeconds);

        return createToken(memberDetails, validity);
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
