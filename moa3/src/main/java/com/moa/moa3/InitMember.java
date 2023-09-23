package com.moa.moa3;

import com.moa.moa3.entity.member.Member;
import com.moa.moa3.entity.member.profile.Category;
import com.moa.moa3.entity.member.profile.ProfileJob;
import com.moa.moa3.entity.member.profile.ProfileSkill;
import com.moa.moa3.repository.member.MemberRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        private static final Category[] skills = {
                Category.PYTHON, Category.JAVA, Category.JAVASCRIPT,Category.REACT, Category.SPRING
        };
        private static final Category[] jobs = {
                Category.FRONTEND, Category.BACKEND, Category.STUDENT
        };

        public void init() {
            for (int i = 1; i <= 100; i++) {
                Member member = new Member("test" + i, "test" + i + "@com", "test.com", "local");

                List<Integer> randomNumbers = getRandomNumbers();
                for (Integer skillIdx : randomNumbers) {
                    member.getProfile().getSkills().add(new ProfileSkill(skills[skillIdx]));
                }

                Random random = new Random();
                int jobIdx = random.nextInt(jobs.length);
                member.getProfile().setJob(new ProfileJob(jobs[jobIdx]));
                memberRepository.save(member);
            }
        }

        private List<Integer> getRandomNumbers() {
            Random random = new Random();
            int k = random.nextInt(skills.length);
            List<Integer> list = new ArrayList<>();
            for (int i = 0; i < skills.length ; i++) {
                list.add(i);
            }

            List<Integer> result = new ArrayList<>();
            for (int i = 0; i < k; i++) {
                int randomIndex = random.nextInt(list.size());
                result.add(list.remove(randomIndex));
            }

            return result;
        }
    }
}
