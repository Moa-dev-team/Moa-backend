package com.moa2.repository.member;

import com.moa2.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);


    @Query("SELECT m FROM Member m JOIN FETCH m.authorities WHERE m.email = :email")
    Optional<Member> findByEmailEagerly(@Param("email") String email);
}
