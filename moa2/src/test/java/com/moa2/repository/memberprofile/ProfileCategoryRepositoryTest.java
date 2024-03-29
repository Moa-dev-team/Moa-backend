package com.moa2.repository.memberprofile;

import com.moa2.domain.member.Member;
import com.moa2.domain.memberprofile.MemberProfile;
import com.moa2.domain.memberprofile.category.Category;
import com.moa2.domain.memberprofile.profilecategory.ProfileInterestTech;
import com.moa2.domain.memberprofile.profilecategory.ProfileJob;
import com.moa2.domain.memberprofile.profilecategory.ProfileTechStack;
import com.moa2.domain.memberprofile.url.SocialLink;
import com.moa2.domain.memberprofile.url.UrlLink;
import com.moa2.repository.member.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ProfileCategoryRepositoryTest {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    void saveDataTest() {
        Member member = new Member();
        member.setNickname("test");
        MemberProfile memberProfile = new MemberProfile();
        memberProfile.setStatusMessage("test message");

        List<UrlLink> urlLinks = List.of(
                new UrlLink("test urlLink1"), new UrlLink("test urlLink2"));
        memberProfile.setUrlLinks(urlLinks);

        List<SocialLink> socialLinks = List.of(
                new SocialLink("test socialLink1"), new SocialLink("test socialLink2"));
        memberProfile.setSocialLinks(socialLinks);

        Category backend = categoryRepository.findByName("백엔드").get();
        ProfileJob profileCategory = new ProfileJob();
        profileCategory.setCategory(backend);
        memberProfile.setProfileJobs(List.of(profileCategory));

        Category react = categoryRepository.findByName("React").get();
        Category html = categoryRepository.findByName("HTML").get();
        List<ProfileInterestTech> profileInterestTechs = List.of(
                new ProfileInterestTech(react), new ProfileInterestTech(html));
        memberProfile.setProfileInterestTechs(profileInterestTechs);

        Category java = categoryRepository.findByName("Java").get();
        Category spring = categoryRepository.findByName("Spring").get();
        List<ProfileTechStack> profileTechStacks = List.of(
                new ProfileTechStack(java), new ProfileTechStack(spring));
        memberProfile.setProfileTechStacks(profileTechStacks);

        member.setMemberProfile(memberProfile);
        memberRepository.save(member);

        Member findMember = memberRepository.findByIdWithMemberProfile(member.getId()).get();
        assertEquals(findMember.getMemberProfile().getStatusMessage(), "test message");
        assertEquals(findMember.getMemberProfile().getUrlLinks().get(0).getUrl(), "test urlLink1");
        assertEquals(findMember.getMemberProfile().getUrlLinks().get(1).getUrl(), "test urlLink2");
        assertEquals(findMember.getMemberProfile().getSocialLinks().get(0).getUrl(), "test socialLink1");
        assertEquals(findMember.getMemberProfile().getSocialLinks().get(1).getUrl(), "test socialLink2");
        assertEquals(findMember.getMemberProfile().getProfileJobs().get(0).getCategory().getName(), "백엔드");
        assertEquals(findMember.getMemberProfile().getProfileInterestTechs().get(0).getCategory().getName(), "React");
        assertEquals(findMember.getMemberProfile().getProfileInterestTechs().get(1).getCategory().getName(), "HTML");
        assertEquals(findMember.getMemberProfile().getProfileTechStacks().get(0).getCategory().getName(), "Java");
        assertEquals(findMember.getMemberProfile().getProfileTechStacks().get(1).getCategory().getName(), "Spring");
    }
}