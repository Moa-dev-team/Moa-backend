package com.moa.moa3.service.oauth;

import com.moa.moa3.api.oauth.OAuthApi;
import com.moa.moa3.dto.jwt.AtRt;
import com.moa.moa3.dto.oauth.*;
import com.moa.moa3.entity.member.Member;
import com.moa.moa3.entity.member.MemberFactory;
import com.moa.moa3.exception.oauth.DuplicateLoginFailureException;
import com.moa.moa3.jwt.JwtTokenProvider;
import com.moa.moa3.jwt.JwtTokenService;
import com.moa.moa3.repository.member.MemberRepository;
import com.moa.moa3.security.MemberDetails;
import com.moa.moa3.service.member.MemberService;
import com.moa.moa3.util.oauth.OAuthProviderFactory;
import com.moa.moa3.util.oauth.userprofile.UserProfileMapperFactory;
import com.moa.moa3.validation.jwt.JwtTokenValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuthService {

    private final OAuthProviderFactory oAuthProviderFactory;
    private final MemberService memberService;
    private final JwtTokenService jwtTokenService;

    public LoginSuccess login(String provider, String code) {
        UserProfile userProfile = getUserProfile(provider, code);

        Member member = memberService.getOrCreateMember(userProfile, provider);
        AtRt atRt = jwtTokenService.createAtRt(member);

        return new LoginSuccess(member, atRt);
    }

    public AtRt refresh(String refreshToken) {
        return jwtTokenService.refresh(refreshToken);
    }

    private UserProfile getUserProfile(String provider, String code) {
        OAuthProvider oAuthProvider = oAuthProviderFactory.getProvider(provider);

        OAuthAccessTokenResponse accessTokenResponse = OAuthApi.getAccessTokenResponse(code, oAuthProvider);
        Map<String, Object> userAttributes = OAuthApi.getUserAttributes(
                oAuthProvider, accessTokenResponse.getAccessToken());
        return UserProfileMapperFactory.getMapper(provider).map(userAttributes);
    }
}
