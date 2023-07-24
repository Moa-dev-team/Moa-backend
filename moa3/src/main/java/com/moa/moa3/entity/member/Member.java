package com.moa.moa3.entity.member;

import com.moa.moa3.entity.BaseEntity;
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
}
