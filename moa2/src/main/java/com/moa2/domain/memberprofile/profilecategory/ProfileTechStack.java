package com.moa2.domain.memberprofile.profilecategory;

import com.moa2.domain.memberprofile.category.Category;
import jakarta.persistence.Entity;

@Entity
public class ProfileTechStack extends ProfileCategory{
    public ProfileTechStack() {
    }

    public ProfileTechStack(Category category) {
        super(category);
    }
}
