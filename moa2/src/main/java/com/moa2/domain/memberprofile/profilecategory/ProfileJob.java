package com.moa2.domain.memberprofile.profilecategory;

import com.moa2.domain.memberprofile.category.Category;
import jakarta.persistence.Entity;

@Entity
public class ProfileJob extends ProfileCategory{
    public ProfileJob() {
    }

    public ProfileJob(Category category) {
        super(category);
    }
}
