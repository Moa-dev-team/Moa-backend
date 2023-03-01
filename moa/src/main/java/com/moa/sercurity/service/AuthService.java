package com.moa.sercurity.service;

import com.moa.sercurity.dto.LoginDto;
import com.moa.sercurity.dto.TokenDto;
import com.moa.sercurity.redis.RedisService;
import com.moa.sercurity.utill.JwtTokenProvider;
import java.util.Date;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class AuthService {

  private final JwtTokenProvider jwtTokenProvider;
  private final AuthenticationManagerBuilder authenticationManagerBuilder;
  private final RedisService redisService;
  private final String SERVER = "Server";

  //"Bearer {AT}"에서 {AT} 가져오기
  public String resolveToken(String requestAccessTokenInHeader) {
    if (requestAccessTokenInHeader != null && requestAccessTokenInHeader.startsWith("Bearer ")) {
      return requestAccessTokenInHeader.substring(7);
    }
    return null;
  }

  //Principal 가져오기
  public String getPrincipal(String requestAccessToken) {
    return jwtTokenProvider.getAuthentication(requestAccessToken).getName();
  }

  //권한(Role) 이름들 가져오기
  public String getAuthorities(Authentication authentication) {
    return authentication.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.joining(","));
  }

  //RT Redis 에 저장.
  @Transactional
  public void saveRefreshToken(String provider, String principal, String refreshToken) {
    redisService.setValuesWithTimeout("RT(" + provider + "):" + principal, refreshToken,
        jwtTokenProvider.getTokenExpirationTime(refreshToken));

  }

  //토큰 발급.
  @Transactional
  public TokenDto generateToken(String provider, String email, String authorities) {
    if (redisService.getValues("RT(" + provider + "):" + email) != null) {
      redisService.deleteValues("RT(" + provider + "):" + email);
    }//RT가 이미 있을 경우

    TokenDto tokenDto = jwtTokenProvider.createToken(email, authorities);
    saveRefreshToken(provider, email, tokenDto.getRefreshToken());
    //AT, RT 생성 및 Redis 에 RT 저장
    return tokenDto;
  }

  // AT가 만료일자만 초과한 유효한 토큰인지 검사
  public boolean validate(String requestAccessTokenInHeader) {
    String requestAccessToken = resolveToken(requestAccessTokenInHeader);
    return jwtTokenProvider.validateAccessTokenOnlyExpired(requestAccessToken); // true = 재발급
  }

  // 토큰 재발급: validate 메서드가 true 반환할 때만 사용 -> AT, RT 재발급
  @Transactional
  public TokenDto reissue(String requestAccessTokenInHeader, String requestRefreshToken) {
    String requestAccessToken = resolveToken(requestAccessTokenInHeader);

    Authentication authentication = jwtTokenProvider.getAuthentication(requestAccessToken);
    String principal = getPrincipal(requestAccessToken);

    String refreshTokenInRedis = redisService.getValues("RT(" + SERVER + "):" + principal);

    if (refreshTokenInRedis == null) {
      return null; // 재로그인 요청
    }//Redis 에 저장되어 있는 RT가 없는경우

    if (!jwtTokenProvider.validateRefreshToken(requestRefreshToken) || !refreshTokenInRedis.equals(
        refreshTokenInRedis)) {
      redisService.deleteValues("RT(" + SERVER + "):" + principal); //탈취 가능성 (삭제)
      return null; // 재로그인 요청
    }

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String authorities = getAuthorities(authentication);

    //토큰 재발급 및 Redis 업데이트
    redisService.deleteValues("RT(" + SERVER + "):" + principal);
    TokenDto newTokenDto = jwtTokenProvider.createToken(principal, authorities);
    saveRefreshToken(SERVER, principal, newTokenDto.getRefreshToken());
    return newTokenDto;

  }

  /**
   * 로그인
   *
   * @param loginDto
   * @return TokenDto
   */
  @Transactional
  public TokenDto login(LoginDto loginDto) {
    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
        loginDto.getEmail(), loginDto.getPassword());

    Authentication authentication = authenticationManagerBuilder
        .getObject()
        .authenticate(authenticationToken);

    SecurityContextHolder
        .getContext()
        .setAuthentication(authentication);

    return generateToken(SERVER, authentication.getName(), getAuthorities(authentication));
  }

  /**
   * 로그 아웃
   *
   * @param requestAccessTokenInHeader
   */
  @Transactional
  public void logout(String requestAccessTokenInHeader) {
    String requestAccessToken = resolveToken(requestAccessTokenInHeader);
    String principal = getPrincipal(requestAccessToken);

    //Redis 에 있는 RT 삭제
    String refreshTokenInRedis = redisService.getValues("RT(" + SERVER + "):" + principal);
    if (refreshTokenInRedis != null) {
      redisService.deleteValues("RT(" + SERVER + "):" + principal);
    }

    //Redis 에 로그아웃 처리한 AT 저장
    long expiration =
        jwtTokenProvider.getTokenExpirationTime(requestAccessToken) - new Date().getTime();
    redisService.setValuesWithTimeout(requestAccessToken, "logout", expiration);
  }

}
