package com.moa2.domain.memberprofile.profilecategory;

import com.moa2.domain.memberprofile.category.Category;
import jakarta.persistence.Entity;

@Entity
public class ProfileInterestTech extends ProfileCategory{
    public ProfileInterestTech() {
    }

    public ProfileInterestTech(Category category) {
        super(category);
    }
}
