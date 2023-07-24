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
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtTokenService jwtTokenService;

    public LoginSuccess login(String provider, String code) {
        UserProfile userProfile = getUserProfile(provider, code);

        Member member = memberService.getOrCreateMember(userProfile, provider);

        MemberDetails memberDetails = new MemberDetails(member);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                memberDetails, null, memberDetails.getAuthorities());
        AtRt atRt = jwtTokenProvider.createAtRt(authentication);

        return new LoginSuccess(member, atRt);
    }

    public RefreshSuccess refresh(String refreshToken) {
        Authentication authentication = jwtTokenService.createAuthenticationWithRt(refreshToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        AtRt atRt = jwtTokenProvider.createAtRt(authentication);

        //이전에 발급된 at, rt token 을 삭제시키는 로직을 넣을지 고민 중입니다.

        return new RefreshSuccess(atRt.getAccessToken(), atRt.getRefreshToken());
    }


    private UserProfile getUserProfile(String provider, String code) {
        OAuthProvider oAuthProvider = oAuthProviderFactory.getProvider(provider);

        OAuthAccessTokenResponse accessTokenResponse = OAuthApi.getAccessTokenResponse(code, oAuthProvider);
        Map<String, Object> userAttributes = OAuthApi.getUserAttributes(
                oAuthProvider, accessTokenResponse.getAccessToken());
        return UserProfileMapperFactory.getMapper(provider).map(userAttributes);
    }
}
