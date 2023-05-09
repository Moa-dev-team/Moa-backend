package com.moa2.repository.memberprofile;

import com.moa2.domain.memberprofile.profilecategory.ProfileCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileCategoryRepository extends JpaRepository<ProfileCategory, Long> {
}
