package com.moa2.repository.memberprofile;

import com.moa2.domain.memberprofile.url.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlRepository extends JpaRepository<Url, Long> {
}
