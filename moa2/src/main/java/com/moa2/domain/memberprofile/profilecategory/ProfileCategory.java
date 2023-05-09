package com.moa2.domain.memberprofile.profilecategory;

import com.moa2.domain.memberprofile.MemberProfile;
import com.moa2.domain.memberprofile.category.Category;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class ProfileCategory {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "member_profile_id")
    private MemberProfile memberProfile;

    public ProfileCategory() {
    }
    public ProfileCategory(Category category) {
        this.category = category;
    }
}
