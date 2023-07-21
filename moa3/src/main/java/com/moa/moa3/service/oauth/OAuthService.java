package com.moa.moa3.service.oauth;

import com.moa.moa3.api.oauth.OAuthApi;
import com.moa.moa3.dto.oauth.*;
import com.moa.moa3.entity.member.Member;
import com.moa.moa3.entity.member.MemberFactory;
import com.moa.moa3.exception.oauth.DuplicateLoginFailureException;
import com.moa.moa3.jwt.JwtTokenProvider;
import com.moa.moa3.jwt.JwtTokenService;
import com.moa.moa3.repository.member.MemberRepository;
import com.moa.moa3.security.MemberDetails;
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
    private final MemberRepository memberRepository;
    private final MemberFactory memberFactory;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtTokenService jwtTokenService;

    public LoginSuccess login(String provider, String code) {
        UserProfile userProfile = getUserProfile(provider, code);

        Optional<Member> memberOptional = memberRepository.findByEmailWithAuthorities(userProfile.getEmail());
        Member member;
        boolean firstLogin = true;
        // 이미 가입된 계정이 존재할 경우
        if (memberOptional.isPresent()) {
            member = memberOptional.get();
            // 이미 가입된 계정의 소셜 사이트와 현재 가입요청한 소셜 사이트가 다를 경우 - 로그인 실패
            if (!member.getOAuthProvider().equals(provider)) {
                throw new DuplicateLoginFailureException(member.getOAuthProvider());
            }
            // 이미 가입된 계정의 소셜 사이트와 현재 가입요청한 소셜 사이트가 같을 경우 - 로그인 성공
            firstLogin = false;
        }
        // 처음 가입한 경우 - 로그인 성공
        else {
            member = memberFactory.createUser(userProfile, provider);
        }

        MemberDetails memberDetails = new MemberDetails(member);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                memberDetails, null, memberDetails.getAuthorities());
        String accessToken = jwtTokenProvider.createAccessToken(authentication);
        String refreshToken = jwtTokenProvider.createRefreshToken(authentication);

        return new LoginSuccess(member, firstLogin, accessToken, refreshToken);
    }

    public RefreshSuccess refresh(String refreshToken) {
        Authentication authentication = jwtTokenService.createAuthentication(refreshToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String newAccessToken = jwtTokenProvider.createAccessToken(authentication);
        String newRefreshToken = jwtTokenProvider.createRefreshToken(authentication);

        //이전에 발급된 at, rt token 을 삭제시키는 로직을 넣을지 고민 중입니다.

        return new RefreshSuccess(newAccessToken, newRefreshToken);
    }


    private UserProfile getUserProfile(String provider, String code) {
        OAuthProvider oAuthProvider = oAuthProviderFactory.getProvider(provider);

        OAuthAccessTokenResponse accessTokenResponse = OAuthApi.getAccessTokenResponse(code, oAuthProvider);
        Map<String, Object> userAttributes = OAuthApi.getUserAttributes(
                oAuthProvider, accessTokenResponse.getAccessToken());
        return UserProfileMapperFactory.getMapper(provider).map(userAttributes);
    }
}
