package com.moa2.repository.memberprofile;

import com.moa2.domain.memberprofile.MemberProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberProfileRepository extends JpaRepository<MemberProfile, Long> {
}
