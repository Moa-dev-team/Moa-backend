package com.moa2.service.member;

import com.moa2.domain.member.Member;
import com.moa2.domain.memberprofile.MemberProfile;
import com.moa2.domain.memberprofile.category.Category;
import com.moa2.domain.memberprofile.profilecategory.ProfileInterestTech;
import com.moa2.domain.memberprofile.profilecategory.ProfileJob;
import com.moa2.domain.memberprofile.profilecategory.ProfileTechStack;
import com.moa2.domain.memberprofile.url.SocialLink;
import com.moa2.domain.memberprofile.url.UrlLink;
import com.moa2.dto.auth.request.SignupDto;
import com.moa2.dto.memberprofile.request.UpdateProfileRequestDto;
import com.moa2.dto.memberprofile.response.MemberProfileResponseDto;
import com.moa2.repository.member.AuthorityRepository;
import com.moa2.repository.member.MemberRepository;
import com.moa2.repository.memberprofile.CategoryRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;
    private final CategoryRepository categoryRepository;

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

    @Transactional
    public void updateMemberProfile(Long memberId, UpdateProfileRequestDto updateProfileRequestDto) {
        Member member = memberRepository.findByIdWithMemberProfile(memberId)
                .orElseThrow(() -> new IllegalArgumentException("there is no member with that id"));
        member.setNickname(updateProfileRequestDto.getNickname());
        member.setImageUrl(updateProfileRequestDto.getImageUrl());

        MemberProfile newProfile = new MemberProfile();
        newProfile.setStatusMessage(updateProfileRequestDto.getStatusMessage());
        newProfile.setUrlLinks(updateProfileRequestDto.getUrlLinks()
                .stream().map(UrlLink::new).toList());
        newProfile.setSocialLinks(updateProfileRequestDto.getSocialLinks()
                .stream().map(SocialLink::new).toList());

        List<Category> jobs = updateProfileRequestDto.getJobs()
                .stream().map(job -> categoryRepository.findByName(job)
                        .orElseThrow(
                        () -> new IllegalArgumentException("there is no category with " + job)
                        )
                ).toList();
        newProfile.setProfileJobs(jobs.stream().map(ProfileJob::new).toList());

        List<Category> interestTechs = updateProfileRequestDto.getInterestTechs()
                .stream().map(interestTech -> categoryRepository.findByName(interestTech)
                        .orElseThrow(
                                () -> new IllegalArgumentException("there is no category with " +interestTech)
                        )
                ).toList();
        newProfile.setProfileInterestTechs(interestTechs.stream().map(ProfileInterestTech::new).toList());

        List<Category> techStacks = updateProfileRequestDto.getTechStacks()
                .stream().map(techStack -> categoryRepository.findByName(techStack)
                        .orElseThrow(
                                () -> new IllegalArgumentException("there is no category with " + techStack)
                        )
                ).toList();
        newProfile.setProfileTechStacks(techStacks.stream().map(ProfileTechStack::new).toList());

        member.setMemberProfile(newProfile);
    }
}