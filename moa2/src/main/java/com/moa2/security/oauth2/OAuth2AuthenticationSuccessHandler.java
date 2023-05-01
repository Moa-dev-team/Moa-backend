package com.moa2.security.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moa2.dto.auth.TokenDto;
import com.moa2.dto.auth.response.FirstLoginResponseDto;
import com.moa2.dto.auth.response.SuccessLoginResponseDto;
import com.moa2.security.jwt.JwtTokenProvider;
import com.moa2.security.userdetails.MemberDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtTokenProvider jwtTokenProvider;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        if (response.isCommitted()) {
            log.debug("Response has already been committed. Unable to redirect to ");
            return;
        }
        clearAuthenticationAttributes(request, response);

        TokenDto tokenDto = jwtTokenProvider.createTokens(authentication);
        MemberDetails memberDetails = (MemberDetails) authentication.getPrincipal();

        Long memberId = memberDetails.getMemberId();
        Long accessTokenExpirationInMilliSeconds =
                jwtTokenProvider.getClaims(tokenDto.getAccessToken()).getExpiration().getTime();
        Long refreshTokenExpirationInSeconds =
                jwtTokenProvider.getClaims(tokenDto.getRefreshToken()).getExpiration().getTime()/1000;

        HttpCookie cookie = ResponseCookie.from("refreshToken", tokenDto.getRefreshToken())
                .path("/")
                .maxAge(refreshTokenExpirationInSeconds)
//                .secure(true)
                .httpOnly(true)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        SuccessLoginResponseDto successLoginResponseDto;
        if (memberDetails.getIsFirstLogin()) {
            successLoginResponseDto = new FirstLoginResponseDto(
                    tokenDto.getAccessToken(),
                    accessTokenExpirationInMilliSeconds,
                    memberDetails.getImageUrl(),
                    memberDetails.getName()
                    );
        }
        else {
            successLoginResponseDto = new SuccessLoginResponseDto(
                    tokenDto.getAccessToken(),
                    accessTokenExpirationInMilliSeconds
            );
        }

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(successLoginResponseDto);

        response.setStatus(HttpStatus.CREATED.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonString);
        response.getWriter().flush();
        response.getWriter().close();
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }
}
