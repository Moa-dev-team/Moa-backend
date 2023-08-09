package com.moa.moa3.jwt;

import com.moa.moa3.entity.member.Member;
import com.moa.moa3.security.MemberDetails;
import com.moa.moa3.service.member.MemberService;
import com.moa.moa3.validation.jwt.JwtTokenValidator;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final MemberService memberService;
    private final JwtTokenValidator jwtTokenValidator;
    private final JwtTokenProvider jwtTokenProvider;

    private Authentication createAuthentication(String token) {
        jwtTokenValidator.validateToken(token);
        Claims claims = jwtTokenProvider.getClaims(token);

        Collection<? extends GrantedAuthority> authorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList(claims.get("authorities").toString());
        Long memberId = claims.get("memberId", Long.class);

        Member member = memberService.findById(memberId);
        MemberDetails memberDetails = new MemberDetails(member);

        return new UsernamePasswordAuthenticationToken(memberDetails, token, authorities);
    }

    public Authentication createAuthenticationWithAt(String accessToken) {
        jwtTokenValidator.validateAccessToken(accessToken);
        return createAuthentication(accessToken);
    }

    public Authentication createAuthenticationWithRt(String refreshToken) {
        jwtTokenValidator.validateRefreshToken(refreshToken);
        return createAuthentication(refreshToken);
    }
}
