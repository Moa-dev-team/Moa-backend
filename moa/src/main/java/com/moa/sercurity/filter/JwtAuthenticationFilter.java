package com.moa.sercurity.filter;

import com.moa.sercurity.utill.JwtTokenProvider;
import io.jsonwebtoken.IncorrectClaimException;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtTokenProvider jwtTokenProvider;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    String accessToken = resolveToken(request);
    try {
      if (accessToken != null && jwtTokenProvider.validateAccessToken(accessToken)) {
        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.debug("SecurityContextHolder에 Authentication을 저장하였습니다.");
      }
    } catch (IncorrectClaimException e) {
      SecurityContextHolder.clearContext();
      log.debug("JWT Token이 올바르지 않습니다.");
      response.sendError(403);
    } catch (UsernameNotFoundException e) {
      SecurityContextHolder.clearContext();
      log.debug("유저를 찾을 수 없습니다.");
      response.sendError(403);
    }
    filterChain.doFilter(request, response);

  }

  public String resolveToken(HttpServletRequest httpServletRequest) {
    String bearerToken = httpServletRequest.getHeader("Authorization");
    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }
}
