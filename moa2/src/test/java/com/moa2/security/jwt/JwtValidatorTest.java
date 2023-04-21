package com.moa2.security.jwt;

import com.moa2.domain.member.Member;
import com.moa2.dto.auth.LoginDto;
import com.moa2.dto.auth.SignupDto;
import com.moa2.dto.auth.TokenDto;
import com.moa2.repository.member.MemberRepository;
import com.moa2.service.auth.AuthService;
import com.moa2.service.member.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class JwtValidatorTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtValidator jwtValidator;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void beforeEach() {
        memberRepository.deleteAll();

        SignupDto signupDto = new SignupDto();
        signupDto.setNickname("test");
        signupDto.setEmail("test@example.com");
        signupDto.setPassword("password");
        Member member = memberService.createUser(signupDto);
        memberService.register(member);
    }
    @Test
    void good_AT_validation_after_login() {
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("test@example.com");
        loginDto.setPassword("password");
        TokenDto tokenDto = authService.login(loginDto);

        assertThat(jwtValidator.validateToken(tokenDto.getAccessToken())).isTrue();
        assertThat(jwtValidator.validateAccessToken(tokenDto.getAccessToken())).isTrue();
    }

    @Test
    void bad_AT_validation_after_login() {
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("test@example.com");
        loginDto.setPassword("password");
        TokenDto tokenDto = authService.login(loginDto);

        String badAccessToken = tokenDto.getAccessToken() + "bad";

        assertThat(jwtValidator.validateToken(badAccessToken)).isFalse();
    }

    @Test
    void AT_validation_after_logout() {
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("test@example.com");
        loginDto.setPassword("password");

        TokenDto tokenDto = authService.login(loginDto);
        authService.logout("Bearer " + tokenDto.getAccessToken());

        assertThat(jwtValidator.validateAccessToken(tokenDto.getAccessToken())).isFalse();
    }

    @Test
    void AT_validation_after_expired() {
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("test@example.com");
        loginDto.setPassword("password");
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        String accessToken = jwtTokenProvider.createTokenWithSeconds(authentication, false, 0L);

        assertThat(jwtValidator.validateToken(accessToken)).isFalse();
    }

    @Test
    void good_RT_validation_after_login() {
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("test@example.com");
        loginDto.setPassword("password");
        TokenDto tokenDto = authService.login(loginDto);

        assertThat(jwtValidator.validateRefreshToken(tokenDto.getRefreshToken())).isTrue();
    }

    @Test
    void bad_RT_validation_after_login() {
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("test@example.com");
        loginDto.setPassword("password");
        TokenDto tokenDto = authService.login(loginDto);
        String badRefreshToken = tokenDto.getRefreshToken() + "bad";

        assertThat(jwtValidator.validateRefreshToken(badRefreshToken)).isFalse();
    }

    @Test
    void RT_validation_after_logout() {
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("test@example.com");
        loginDto.setPassword("password");
        TokenDto tokenDto = authService.login(loginDto);
        authService.logout("Bearer " + tokenDto.getAccessToken());

        assertThat(jwtValidator.validateRefreshToken(tokenDto.getRefreshToken())).isFalse();
    }

    @Test
    void RT_validation_after_expired() {
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("test@example.com");
        loginDto.setPassword("password");        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        String refreshToken = jwtTokenProvider.createTokenWithSeconds(authentication, true, 0L);

        assertThat(jwtValidator.validateRefreshToken(refreshToken)).isFalse();
    }

    @Test
    void RT_validation_after_refresh() {
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("test@example.com");
        loginDto.setPassword("password");
        TokenDto tokenDto = authService.login(loginDto);

        TokenDto refreshedTokenDto = authService.refresh(tokenDto.getRefreshToken());

        assertThat(jwtValidator.validateRefreshToken(refreshedTokenDto.getRefreshToken())).isTrue();
    }

}