package com.moa.moa3.oauth;

import com.moa.moa3.dto.oauth.UserProfile;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class OAuthValidationTest {
    @Test
    public void UserProfileValidator_test() {
        // email 이 비어있는 경우
        assertThrows(IllegalArgumentException.class, () -> {
            UserProfile userProfile = new UserProfile(
                    "hosung", "", "123");
        });
        // email 이 null 인 경우

        assertThrows(IllegalArgumentException.class, () -> {
            UserProfile userProfile2 = new UserProfile(
                    "hosung", null, "123");
        });
        // email 이 공백만 포함하고 있는 경우
        assertThrows(IllegalArgumentException.class, () -> {
            UserProfile userProfile3 = new UserProfile(
                    "hosung", "  ", "123");
        });
    }
}
