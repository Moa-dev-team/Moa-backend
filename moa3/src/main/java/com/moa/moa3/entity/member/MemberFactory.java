package com.moa.moa3.entity.member;

import com.moa.moa3.repository.member.AuthorityRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberFactory {

    private final AuthorityRepository authorityRepository;

    public Member createUser(String name, String email, String imageUrl, String oAuthProvider) {
        Member member = Member.builder()
                .name(name)
                .email(email)
                .imageUrl(imageUrl)
                .oAuthProvider(oAuthProvider)
                .build();
        member.getAuthorities().add(authorityRepository.findByName("ROLE_USER"));
        return member;
    }

    public Member createAdmin(String name, String email, String imageUrl, String oAuthProvider) {
        Member member = Member.builder()
                .name(name)
                .email(email)
                .imageUrl(imageUrl)
                .oAuthProvider(oAuthProvider)
                .build();
        member.getAuthorities().add(authorityRepository.findByName("ROLE_ADMIN"));
        member.getAuthorities().add(authorityRepository.findByName("ROLE_USER"));
        return member;
    }

    @PostConstruct
    public void init() {
        authorityRepository.save(new Authority("ROLE_USER"));
        authorityRepository.save(new Authority("ROLE_ADMIN"));
    }
}
