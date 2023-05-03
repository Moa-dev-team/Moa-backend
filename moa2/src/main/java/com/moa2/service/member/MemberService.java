package com.moa2.service.member;

import com.moa2.domain.member.Member;
import com.moa2.dto.auth.request.SignupDto;
import com.moa2.dto.memberprofile.response.MemberProfileResponseDto;
import com.moa2.repository.member.AuthorityRepository;
import com.moa2.repository.member.MemberRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long register(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        memberRepository.findByEmail(member.getEmail())
                .ifPresent(m -> {
                    throw new IllegalArgumentException("already exist email");
                });
    }


    // 미완성
    public String getMemberInfo(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("there is no member with that id"));
        return member.getEmail();
    }

    public Member createUser(@Valid SignupDto signupDto) {
        Member member = new Member();
        member.setNickname(signupDto.getNickname());
        member.setEmail(signupDto.getEmail());
        member.setPassword(passwordEncoder.encode(signupDto.getPassword()));
        member.getAuthorities().add(authorityRepository.findByName("ROLE_USER"));
        return member;
    }

    public MemberProfileResponseDto getMemberProfile(Long memberId) {
        Member member = memberRepository.findByIdWithMemberProfile(memberId)
                .orElseThrow(() -> new IllegalArgumentException("there is no member with that id"));
        return new MemberProfileResponseDto(member);
    }
}