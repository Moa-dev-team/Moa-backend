package com.moa.sercurity.api;

import com.moa.domain.user.service.UserService;
import com.moa.sercurity.dto.LoginDto;
import com.moa.sercurity.dto.SignupDto;
import com.moa.sercurity.dto.TokenDto;
import com.moa.sercurity.service.AuthService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthApiController {

  private final AuthService authService;
  private final UserService userService;
  private final BCryptPasswordEncoder encoder;

  private final long COOKIE_EXPIRATION_SECOND = 60 * 60 * 24 * 90L;

  //회원가입
  @PostMapping("/signup")
  public ResponseEntity signup(@RequestBody @Valid SignupDto signupDto) {
    String encodedPassword = encoder.encode(signupDto.getPassword());
    SignupDto signupDtoWithEncodedPassword = SignupDto.encodePassword(signupDto, encodedPassword);

    userService.registerUser(signupDtoWithEncodedPassword);
    return new ResponseEntity(HttpStatus.OK);
  }

  //로그인
  @PostMapping("/login")
  public ResponseEntity login(@RequestBody @Valid LoginDto loginDto) {

    TokenDto tokenDto = authService.login(loginDto);
    HttpCookie httpCookie = ResponseCookie.from("refresh-token", tokenDto.getRefreshToken())
        .maxAge(COOKIE_EXPIRATION_SECOND)
        .httpOnly(true)
        .secure(true)
        .build();

    return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, httpCookie.toString())
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenDto.getAccessToken())
        .build();
  }

  // 로그아웃
  @PostMapping("/logout")
  public ResponseEntity<?> logout(@RequestHeader("Authorization") String requestAccessToken) {
    authService.logout(requestAccessToken);
    ResponseCookie responseCookie = ResponseCookie.from("refresh-token", "")
        .maxAge(0)
        .path("/")
        .build();

    return ResponseEntity
        .status(HttpStatus.OK)
        .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
        .build();
  }
}
