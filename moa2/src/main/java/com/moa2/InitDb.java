package com.moa2;

import com.moa2.domain.member.Authority;
import com.moa2.repository.member.AuthorityRepository;
import com.moa2.service.member.MemberService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class InitDb {
    private final InitService initService;

    @PostConstruct
    public void init() {
        this.initService.dbInit();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final MemberService memberService;
        private final AuthorityRepository authorityRepository;
        private final PasswordEncoder encoder;
        public void dbInit() {
            authorityRepository.save(new Authority("ROLE_USER"));
            authorityRepository.save(new Authority("ROLE_ADMIN"));
        }
    }
}