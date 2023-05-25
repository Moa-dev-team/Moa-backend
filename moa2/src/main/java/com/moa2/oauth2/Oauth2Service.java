package com.moa2.oauth2;

import com.moa2.domain.member.Member;
import com.moa2.dto.auth.TokenDto;
import com.moa2.dto.auth.response.LoginResponseDto;
import com.moa2.dto.auth.response.Oauth2TokenResponseDto;
import com.moa2.exception.oauth.OAuth2AuthenticationProcessingException;
import com.moa2.repository.member.AuthorityRepository;
import com.moa2.repository.member.MemberRepository;
import com.moa2.security.jwt.JwtTokenProvider;
import com.moa2.security.oauth2.AuthProvider;
import com.moa2.security.userdetails.MemberDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class Oauth2Service {
    private final InMemoryProviderRepository inMemoryProviderRepository;
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthorityRepository authorityRepository;
    private final AuthenticationManager authenticationManager;

    public LoginResponseDto login(String providerName, String code) {
        Oauth2Provider provider = inMemoryProviderRepository.findByProviderName(providerName);
        Oauth2TokenResponseDto tokenResponseDto = getToken(code, provider);
        UserProfile userProfile = getUserProfile(providerName, tokenResponseDto);

        String email = userProfile.getEmail();
        if (email.isEmpty()) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }
        Optional<Member> memberOptional = memberRepository.findByEmailWithAuthorities(email);
        Member member;

        if (memberOptional.isPresent()) {
            member = memberOptional.get();
            if (!member.getProvider().equals(AuthProvider.valueOf(providerName))) {
                throw new OAuth2AuthenticationProcessingException("Looks like you're signed up with " +
                        member.getProvider() + " account. Please use your " + member.getProvider() +
                        " account to login.");
            }
            member.setFirstLogin(false);
        }
        else {
            member = registerNewMember(userProfile, providerName);
        }
        MemberDetails memberDetails = new MemberDetails(member);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(memberDetails, null, memberDetails.getAuthorities())
        );
        TokenDto tokenDto = jwtTokenProvider.createTokens(authentication);
        Long refreshTokenExpirationInMilliSeconds = jwtTokenProvider.getClaims(tokenDto.getRefreshToken()).getExpiration().getTime();

        return LoginResponseDto.builder()
                .name(member.getNickname())
                .imageUrl(member.getImageUrl())
                .isRegistered(member.isFirstLogin())
                .accessToken(tokenDto.getAccessToken())
                .refreshToken(tokenDto.getRefreshToken())
                .refreshTokenExpirationInMilliSeconds(refreshTokenExpirationInMilliSeconds)
                .build();
    }

    private Member registerNewMember(UserProfile userProfile, String providerName) {
        Member member = new Member();
        member.setProvider(AuthProvider.valueOf(providerName));
        member.setNickname(userProfile.getName());
        member.setEmail(userProfile.getEmail());
        member.setImageUrl(userProfile.getImageUrl());
        member.getAuthorities().add(authorityRepository.findByName("ROLE_USER"));

        return memberRepository.save(member);
    }

    private MultiValueMap<String, String> tokenRequest(String code, Oauth2Provider provider) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("code", code);
        formData.add("grant_type", "authorization_code");
        formData.add("redirect_uri", provider.getRedirectUri());
        return formData;
    }

    private Oauth2TokenResponseDto getToken(String code, Oauth2Provider provider) {
        return WebClient.create()
                .post()
                .uri(provider.getTokenUri())
                .headers(header -> {
                    header.setBasicAuth(provider.getClientId(), provider.getClientSecret());
                    header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                    header.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
                    header.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
                })
                .bodyValue(tokenRequest(code, provider))
                .retrieve()
                .bodyToMono(Oauth2TokenResponseDto.class)
                .block();
    }
    private UserProfile getUserProfile(String providerName, Oauth2TokenResponseDto tokenResponseDto) {
        Oauth2Provider provider = inMemoryProviderRepository.findByProviderName(providerName);
        Map<String, Object> userAttributes = getUserAttributes(provider, tokenResponseDto);
        return Oauth2Attributes.extract(providerName, userAttributes);
    }

    private Map<String, Object> getUserAttributes(Oauth2Provider provider, Oauth2TokenResponseDto tokenResponse) {
        return WebClient.create()
                .get()
                .uri(provider.getUserInfoUri())
                .headers(header -> header.setBearerAuth(tokenResponse.getAccessToken()))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
    }
}
