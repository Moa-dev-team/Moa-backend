package com.moa2.security.oauth2;

import com.moa2.domain.member.Member;
import com.moa2.exception.oauth.OAuth2AuthenticationProcessingException;
import com.moa2.repository.member.AuthorityRepository;
import com.moa2.repository.member.MemberRepository;
import com.moa2.security.oauth2.user.OAuth2UserInfo;
import com.moa2.security.oauth2.user.OAuth2UserInfoFactory;
import com.moa2.security.userdetails.MemberDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;
    private final AuthorityRepository authorityRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        try {
            return processOAuth2User(userRequest, oAuth2User);
        } catch (Exception ex) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }


    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(
                userRequest.getClientRegistration().getRegistrationId(),
                oAuth2User.getAttributes()
        );

        String email = oAuth2UserInfo.getEmail();
        if (email.isEmpty()) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }

        Optional<Member> memberOptional = memberRepository.findByEmailWithAuthorities(oAuth2UserInfo.getEmail());
        Member member;

        if (memberOptional.isPresent()) {
            member = memberOptional.get();
            if (!member.getProvider().equals(AuthProvider.valueOf(userRequest.getClientRegistration().getRegistrationId()))) {
                throw new OAuth2AuthenticationProcessingException("Looks like you're signed up with " +
                        member.getProvider() + " account. Please use your " + member.getProvider() +
                        " account to login.");
            }
            member = updateExistingUser(member, oAuth2UserInfo);
        } else {
            member = registerNewUser(userRequest, oAuth2UserInfo);
        }

        return MemberDetails.createWithAttributes(member, oAuth2User.getAttributes());
    }

    private Member registerNewUser(OAuth2UserRequest userRequest, OAuth2UserInfo oAuth2UserInfo) {
        Member member = new Member();

        member.setProvider(AuthProvider.valueOf(userRequest.getClientRegistration().getRegistrationId()));
        member.setProviderId(oAuth2UserInfo.getId());
        member.setNickname(oAuth2UserInfo.getName());
        member.setEmail(oAuth2UserInfo.getEmail());
        member.setImageUrl(oAuth2UserInfo.getImageUrl());
        member.getAuthorities().add(authorityRepository.findByName("ROLE_USER"));

        return memberRepository.save(member);
    }


    private Member updateExistingUser(Member existingMember, OAuth2UserInfo oAuth2UserInfo) {
        existingMember.setFirstLogin(false);
        return memberRepository.save(existingMember);
    }
}
