package com.moa.moa3.entity.member;

import com.moa.moa3.dto.member.ProfileUpdateRequest;
import com.moa.moa3.entity.BaseEntity;
import com.moa.moa3.entity.chat.ChatRoomsMembersJoin;
import com.moa.moa3.entity.member.profile.Profile;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

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

    @ElementCollection
    @CollectionTable(name = "member_last_access", joinColumns = @JoinColumn(name = "member_id"))
    @MapKeyColumn(name = "chat_room_id")
    @Column(name = "last_access_time")
    private Map<Long, LocalDateTime> lastAccessTimes = new HashMap<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatRoomsMembersJoin> chatRoomsMembersJoins = new ArrayList<>();

    @Builder
    public Member(String name, String email, String imageUrl, String oAuthProvider) {
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
        this.oAuthProvider = oAuthProvider;
        firstLogin = true;
    }

    public void update(ProfileUpdateRequest profileUpdateRequest) {
        name = profileUpdateRequest.getName();
        profile.update(profileUpdateRequest);
    }
}
