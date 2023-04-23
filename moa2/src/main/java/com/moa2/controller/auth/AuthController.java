package com.moa2.controller.auth;

import com.moa2.domain.member.Member;
import com.moa2.dto.auth.LoginDto;
import com.moa2.dto.auth.SignupDto;
import com.moa2.dto.auth.TokenDto;
import com.moa2.service.auth.AuthService;
import com.moa2.service.member.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;
    private final MemberService memberService;

    @PostMapping("/register")
    public ResponseEntity register(@Valid @RequestBody SignupDto signupDto) {
        Member user = memberService.createUser(signupDto);
        memberService.register(user);
        return new ResponseEntity("registration success", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity login(@Valid @RequestBody LoginDto loginDto) {
        TokenDto tokenDto = authService.login(loginDto);
        HttpCookie httpCookie = ResponseCookie.from("refreshToken", tokenDto.getRefreshToken())
                .httpOnly(true)
                // https 에서만 데이터를 보내므로 잠시 주석처리
//                .secure(true)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenDto.getAccessToken())
                .header(HttpHeaders.SET_COOKIE, httpCookie.toString())
                .body("login success");
    }

    @GetMapping("/logout")
    public ResponseEntity logout(@RequestHeader("Authorization") String accessTokenInHeader) {
        authService.logout(accessTokenInHeader);
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

        HttpCookie httpCookie = ResponseCookie.from("refreshToken", tokenDto.getRefreshToken())
                .httpOnly(true)
//                .secure(true)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenDto.getAccessToken())
                .header(HttpHeaders.SET_COOKIE, httpCookie.toString())
                .body("refresh success");
    }


}
