package com.moa.moa3.entity.member;

import com.moa.moa3.dto.member.ProfileUpdateRequest;
import com.moa.moa3.entity.BaseEntity;
import com.moa.moa3.entity.member.profile.Profile;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * MemberFactory 에서 Member를 생성할 것을 권장합니다.
 * Member 생성자 사용시 Authority가 없는 Member가 생성됩니다.
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String imageUrl;

    private String oAuthProvider;
    private boolean firstLogin;

    /**
     * cascade 를 설정했기 때문에 Profile Member data 를 생성하면 Profile 객체도 같이 생성됩니다.
     * 따라서 Member 객체를 생성하면 Profile 객체를 따로 저장하지 않아도 됩니다.
     */

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Profile profile = new Profile();
    public void setFirstLogin(boolean firstLogin){
        this.firstLogin = firstLogin;
    }

    @ManyToMany
    @JoinTable(name = "member_authority",
            joinColumns = @JoinColumn(name = "member_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id"))
    private Set<Authority> authorities = new HashSet<>();

    @Builder
    public Member(String name, String email, String imageUrl, String oAuthProvider) {
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
        this.oAuthProvider = oAuthProvider;
        firstLogin = true;
    }

    public void update(ProfileUpdateRequest profileUpdateRequest) {
        this.name = profileUpdateRequest.getName();
        this.profile.update(profileUpdateRequest);
    }
}
