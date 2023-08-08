package com.moa.moa3.service.member;

import com.moa.moa3.dto.member.MemberProfileResponse;
import com.moa.moa3.dto.oauth.UserProfile;
import com.moa.moa3.entity.member.Member;
import com.moa.moa3.entity.member.MemberFactory;
import com.moa.moa3.exception.oauth.DuplicateLoginFailureException;
import com.moa.moa3.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberFactory memberFactory;

    @Transactional
    public Member getOrCreateMember(UserProfile userProfile, String provider) {
        Optional<Member> memberOptional = memberRepository.findByEmailWithAuthorities(userProfile.getEmail());
        Member member;
        // 이미 가입된 계정이 존재할 경우
        if (memberOptional.isPresent()) {
            member = memberOptional.get();
            // 이미 가입된 계정의 소셜 사이트와 현재 가입요청한 소셜 사이트가 다를 경우 - 로그인 실패
            if (!member.getOAuthProvider().equals(provider)) {
                throw new DuplicateLoginFailureException(member.getOAuthProvider());
            }
            // 이미 가입된 계정의 소셜 사이트와 현재 가입요청한 소셜 사이트가 같을 경우 - 로그인 성공
            member.setFirstLogin(false);
        }
        // 처음 가입한 경우 - 로그인 성공
        else {
            member = memberFactory.createUser(userProfile, provider);
        }

        return member;
    }

    public Member findById(Long id) {
        return memberRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 회원이 존재하지 않습니다.")
        );
    }

    public MemberProfileResponse getMemberProfile(Member member) {
        return new MemberProfileResponse(member);
    }
}
