package com.moa.moa3.entity.member;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * MemberFactory 를 통한 Member 의 create, read 테스트
 */
@SpringBootTest
class MemberFactoryTest {
    @Autowired
    MemberFactory memberFactory;
    @Test
    public void memberFactory() {
        Member user = memberFactory.createUser("test", "test@com", "test.com", "test");
        Member admin = memberFactory.createAdmin("test", "test@com", "test.com", "test");

        assertThat(user.getName()).isEqualTo("test");
        assertThat(user.getAuthorities().size()).isEqualTo(1);
        assertThat(admin.getName()).isEqualTo("test");
        assertThat(admin.getAuthorities().size()).isEqualTo(2);
    }
}