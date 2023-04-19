package com.moa2.service.auth;

import com.moa2.domain.member.Member;
import com.moa2.dto.auth.LoginDto;
import com.moa2.dto.auth.SignupDto;
import com.moa2.repository.member.MemberRepository;
import com.moa2.repository.redis.RedisRepository;
import com.moa2.security.jwt.JwtValidator;
import com.moa2.security.jwt.TokenDto;
import com.moa2.service.member.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AuthServiceTest {

    @Autowired
    private AuthService authService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private AccessTokenService accessTokenService;
    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private JwtValidator jwtValidator;
    @Autowired
    private RedisRepository redisRepository;



    @BeforeEach
    void beforeEach() {
        memberRepository.deleteAll();
        redisRepository.deleteAll();

        SignupDto signupDto = new SignupDto();
        signupDto.setNickname("test");
        signupDto.setEmail("test@example.com");
        signupDto.setPassword("password");
        Member member = memberService.createUser(signupDto);
        memberService.register(member);
    }


    @Test
    void register() {
        SignupDto signupDto = new SignupDto();
        signupDto.setNickname("test");
        signupDto.setEmail("test2@example.com");
        signupDto.setPassword("password");
        Member member1 = memberService.createUser(signupDto);
        memberService.register(member1);

        Member findMember = memberRepository.findByEmail("test2@example.com").get();

        assertThat(findMember).isEqualTo(member1);
    }

    @Test
    void login() {
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("test@example.com");
        loginDto.setPassword("password");
        TokenDto tokenDto = authService.login(loginDto);

        jwtValidator.validateToken(tokenDto.getAccessToken());
        jwtValidator.validateAccessToken(tokenDto.getAccessToken());
        jwtValidator.validateToken(tokenDto.getRefreshToken());
        jwtValidator.validateRefreshToken(tokenDto.getRefreshToken());

        assertThat(accessTokenService.isExistAccessToken(tokenDto.getAccessToken())).isTrue();
        assertThat(refreshTokenService.isExistRefreshToken(tokenDto.getRefreshToken())).isTrue();
    }

    @Test
    void logout() {
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("test@example.com");
        loginDto.setPassword("password");
        TokenDto beforeLogout = authService.login(loginDto);
        authService.logout("Bearer " + beforeLogout.getAccessToken());

        assertThat(accessTokenService.isExistAccessToken(beforeLogout.getAccessToken())).isFalse();
        assertThat(refreshTokenService.isExistRefreshToken(beforeLogout.getRefreshToken())).isFalse();
    }

    @Test
    void refresh() throws InterruptedException {
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("test@example.com");
        loginDto.setPassword("password");
        TokenDto beforeRefresh = authService.login(loginDto);

        // Jwts.builder()는 iat(issued at : 생성시간) field를 기준으로 토큰을 생성한다.
        // iat 의 단위가 second 이기 때문에 1초 이내에 토큰을 생성하면 iat가 같아지는 문제가 발생한다.
        // 따라서 1초 이상의 시간을 두고 토큰을 생성해야 다른 토큰이 생성된다.
        Thread.sleep(1000);
        System.out.println("beforeRefresh.getAccessToken() = " + beforeRefresh.getAccessToken());
        TokenDto afterRefresh = authService.refresh(beforeRefresh.getRefreshToken());
        System.out.println("afterRefresh.getAccessToken() = " + afterRefresh.getAccessToken());

        assertThat(accessTokenService.isExistAccessToken(beforeRefresh.getAccessToken())).isFalse();
        assertThat(refreshTokenService.isExistRefreshToken(beforeRefresh.getRefreshToken())).isFalse();
        assertThat(accessTokenService.isExistAccessToken(afterRefresh.getAccessToken())).isTrue();
        assertThat(refreshTokenService.isExistRefreshToken(afterRefresh.getRefreshToken())).isTrue();
    }
}