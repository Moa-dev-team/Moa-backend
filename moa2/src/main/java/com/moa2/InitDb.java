package com.moa2;

import com.moa2.domain.member.Authority;
import com.moa2.domain.member.Member;
import com.moa2.domain.memberprofile.category.Category;
import com.moa2.repository.member.AuthorityRepository;
import com.moa2.repository.memberprofile.CategoryRepository;
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
        this.initService.authorityInit();
        this.initService.adminInit();
        this.initService.categoryInit();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final AuthorityRepository authorityRepository;
        private final MemberService memberService;
        private final PasswordEncoder passwordEncoder;
        private final CategoryRepository categoryRepository;
        public void authorityInit() {
            authorityRepository.save(new Authority("ROLE_USER"));
            authorityRepository.save(new Authority("ROLE_ADMIN"));
        }
        public void adminInit() {
            Member member = new Member();
            member.setNickname("admin");
            member.setEmail("123@com");
            member.setPassword(passwordEncoder.encode("1111"));
            member.getAuthorities().add(authorityRepository.findByName("ROLE_ADMIN"));
            member.getAuthorities().add(authorityRepository.findByName("ROLE_USER"));
            memberService.register(member);
        }
        public void categoryInit() {
            String[] categories = {
                    "백엔드", "프론트엔드", "HTML", "CSS", "JavaScript", "TypeScript",
                    "React", "Vue", "Svelte", "Nextjs", "Java", "Spring",
                    "Nodejs", "Nestjs", "Go", "Kotlin", "Express", "MySQL",
                    "MongoDB", "Python", "Django", "php", "GraphQL", "Firebase"
            };

            for (String category : categories) {
                Category newCategory = new Category(category);
                categoryRepository.save(newCategory);
            }
        }
    }
}