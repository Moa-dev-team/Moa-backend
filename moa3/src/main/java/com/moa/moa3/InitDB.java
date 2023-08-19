package com.moa.moa3;

import com.moa.moa3.entity.member.Member;
import com.moa.moa3.repository.member.MemberRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Profile("local")
@Component
@RequiredArgsConstructor
public class InitDB {
    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.init();
    }

    @Component
    @RequiredArgsConstructor
    static class InitService {
        private final MemberRepository memberRepository;
        public void init() {
            for (int i = 1; i <= 100; i++) {
                memberRepository.save(new Member("test" + i, "test" + i + "@com", "test.com", "local"));
            }
        }
    }
}
