package com.moa.moa3;

import com.moa.moa3.entity.member.Member;
import com.moa.moa3.entity.member.profile.Category;
import com.moa.moa3.entity.member.profile.ProfileSkill;
import com.moa.moa3.repository.member.MemberRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("local")
@Component
@RequiredArgsConstructor
public class InitMember {
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
                Member member = new Member("test" + i, "test" + i + "@com", "test.com", "local");
                member.getProfile().getSkills().add(new ProfileSkill(Category.values()[i % Category.values().length]));
                member.getProfile().getSkills().add(new ProfileSkill(Category.values()[(i+1) % Category.values().length]));
                memberRepository.save(member);
            }
        }
    }
}
