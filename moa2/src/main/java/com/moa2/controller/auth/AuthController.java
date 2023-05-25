package com.moa2.controller.auth;

import com.moa2.domain.member.Member;
import com.moa2.dto.auth.request.LoginDto;
import com.moa2.dto.auth.request.SignupDto;
import com.moa2.dto.auth.TokenDto;
import com.moa2.dto.auth.response.LoginResponseDto;
import com.moa2.dto.auth.response.SuccessLoginResponseDto;
import com.moa2.service.auth.AuthService;
import com.moa2.service.member.MemberService;
import com.moa2.oauth2.Oauth2Service;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;
    private final MemberService memberService;
    private final Oauth2Service oauth2Service;

    @PostMapping("/register")
    public ResponseEntity register(@Valid @RequestBody SignupDto signupDto) {
        Member user = memberService.createUser(signupDto);
        memberService.register(user);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("register success");
    }

    @PostMapping("/login")
    public ResponseEntity login(@Valid @RequestBody LoginDto loginDto) {
        TokenDto tokenDto = authService.login(loginDto);
        return CreateTokenDtoResponse(tokenDto);
    }

    @GetMapping("login/{provider}")
    public ResponseEntity oauthLogin(@PathVariable String provider, @RequestParam String code) {
        LoginResponseDto loginResponseDto = oauth2Service.login(provider, code);
        Long refreshTokenExpirationFromNowInSeconds = (loginResponseDto.getRefreshTokenExpirationInMilliSeconds() - (new Date().getTime())) / 1000;
        // refreshToken 이 만료하는 시간보다 10분 일찍 쿠키 수명이 끝나도록 설정
        refreshTokenExpirationFromNowInSeconds = Math.max(refreshTokenExpirationFromNowInSeconds - 600, 0);
        HttpCookie cookie = ResponseCookie.from("refreshToken", loginResponseDto.getRefreshToken())
                .path("/")
                .maxAge(refreshTokenExpirationFromNowInSeconds)
//                .secure(true)
                .httpOnly(true)
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(loginResponseDto);
    }

    @GetMapping("/logout")
    public ResponseEntity logout() {
        ResponseCookie responseCookie = ResponseCookie.from("refreshToken", "")
                .maxAge(0)
                .path("/")
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body("logout success");
    }

    @GetMapping("/refresh")
    public ResponseEntity refresh(@CookieValue String refreshToken) {
        TokenDto tokenDto = authService.refresh(refreshToken);
        return CreateTokenDtoResponse(tokenDto);
    }

    private ResponseEntity CreateTokenDtoResponse(TokenDto tokenDto) {
        Long refreshTokenExpirationInMilliSeconds = authService.getExpirationTimeInMilliSeconds(tokenDto.getRefreshToken());
        Long refreshTokenExpirationFromNowInSeconds = (refreshTokenExpirationInMilliSeconds - (new Date().getTime())) / 1000;
        // refreshToken 이 만료하는 시간보다 10분 일찍 쿠키 수명이 끝나도록 설정
        refreshTokenExpirationFromNowInSeconds = Math.max(refreshTokenExpirationFromNowInSeconds - 600, 0);

        HttpCookie cookie = ResponseCookie.from("refreshToken", tokenDto.getRefreshToken())
                .path("/")
                .maxAge(refreshTokenExpirationFromNowInSeconds)
//                .secure(true)
                .httpOnly(true)
                .build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new SuccessLoginResponseDto(tokenDto.getAccessToken(), refreshTokenExpirationInMilliSeconds));
    }
}
