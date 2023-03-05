package com.moa.sercurity.utill;

import static com.moa.sercurity.data.Claim.AUTHORITIES_KEY;
import static com.moa.sercurity.data.Claim.EMAIL_KEY;
import static com.moa.sercurity.data.Claim.URL;
import static com.moa.sercurity.data.JwtType.*;

import com.moa.sercurity.data.userdetail.UserDetailsServiceImpl;
import com.moa.sercurity.dto.TokenDto;
import com.moa.sercurity.redis.RedisService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.security.Key;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@Transactional(readOnly = true)
public class JwtTokenProvider implements InitializingBean {

  private static Key signingKey;

  private final String secretKey;
  private final RedisService redisService;
  private final UserDetailsServiceImpl userDetailsService;

  public JwtTokenProvider(@Value("${jwt.secret}") String secretKey,
      RedisService redisService,
      UserDetailsServiceImpl userDetailsService) {
    this.secretKey = secretKey;
    this.redisService = redisService;
    this.userDetailsService = userDetailsService;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    signingKey = Keys.hmacShaKeyFor(secretKey.getBytes());
  }

  //토큰 Init
  public TokenDto createToken(String userPk, String authorities) {
    Long now = System.currentTimeMillis();
    String accessToken = Jwts.builder()
        .setHeaderParam("typ", "JWT")
        .setHeaderParam("alg", "HS512")
        .setExpiration(new Date(now + ACCESS_TOKEN.getExpiredMilliSeconds()))
        .setSubject("access-token")

        .claim(URL.getKey(), "true")
        .claim(EMAIL_KEY.getKey(), userPk)
        .claim(AUTHORITIES_KEY.getKey(), authorities)

        .signWith(signingKey, SignatureAlgorithm.HS512)
        .compact();

    String refreshToken = Jwts.builder()
        .setHeaderParam("typ", "JWT")
        .setHeaderParam("alg", "HS512")
        .setExpiration(new Date(now + REFRESH_TOKEN.getExpiredMilliSeconds()))
        .setSubject("refresh-token")
        .signWith(signingKey, SignatureAlgorithm.HS512)
        .compact();

    return new TokenDto(accessToken, refreshToken);
  }

  //토큰 정보 Get
  public Claims getClaims(String token) {
    try {
      return Jwts.parserBuilder()
          .setSigningKey(signingKey)
          .build()
          .parseClaimsJws(token)
          .getBody();
    } catch (ExpiredJwtException e) {
      return e.getClaims();
    }
  }

  public Authentication getAuthentication(String token) {
    String email = getClaims(token).get(EMAIL_KEY.getKey()).toString();
    UserDetails userDetails = userDetailsService.loadUserByUsername(email);
    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
  }

  public long getTokenExpirationTime(String token) {
    return getClaims(token)
        .getExpiration()
        .getTime();
  }

  //토큰 Validate
  //refresh 토큰 검증
  public boolean validateRefreshToken(String refreshToken) {
    try {
      if (redisService.getValues(refreshToken).equals("delete")) {
        return false;
      }
      Jwts.parserBuilder()
          .setSigningKey(signingKey)
          .build()
          .parseClaimsJws(refreshToken);
      return true;
    } catch (SignatureException e) {
      log.error("Invalid JWT signature.");
    } catch (MalformedJwtException e) {
      log.error("Invalid JWT token.");
    } catch (ExpiredJwtException e) {
      log.error("Expired JWT token.");
    } catch (UnsupportedJwtException e) {
      log.error("Unsupported JWT token.");
    } catch (IllegalArgumentException e) {
      log.error("JWT claims string is empty");
    } catch (NullPointerException e) {
      log.error("JWT Token is empty");
    }
    return false;
  }

  //access 토큰 검증(filter 에서 사용)
  public boolean validateAccessToken(String accessToken) {
    try {
      if (redisService.getValues(accessToken) != null && redisService.getValues(accessToken)
          .equals("logout")) {
        return false;
      }
      Jwts.parserBuilder()
          .setSigningKey(signingKey)
          .build()
          .parseClaimsJws(accessToken);
      return true;
    } catch (ExpiredJwtException e) {
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  //재발급 검증 API 에서 사용
  public boolean validateAccessTokenOnlyExpired(String accessToken) {
    try {
      return getClaims(accessToken)
          .getExpiration()
          .before(new Date());
    } catch (ExpiredJwtException e) {
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
