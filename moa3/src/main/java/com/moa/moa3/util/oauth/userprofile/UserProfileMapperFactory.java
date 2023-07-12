package com.moa.moa3.util.oauth.userprofile;

import com.moa.moa3.dto.oauth.UserProfile;
import com.moa.moa3.exception.oauth.NotFoundProviderException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

//정적 팩토리 메서드 패턴을 사용했습니다.
public class UserProfileMapperFactory {
    private static class Holder {
        static final UserProfileMapperFactory INSTANCE = new UserProfileMapperFactory();
    }
    private final Map<String, UserProfileMapper> mappers = new HashMap<>();

    private UserProfileMapperFactory() {
        // mapper 가 추가된다면 여기에 추가 해주면 됩니다.
        mappers.put("github", new GithubUserProfileMapper());
    }

    public static UserProfileMapper getMapper(String providerName) {
        return Optional.ofNullable(Holder.INSTANCE.mappers.get(providerName))
                .orElseThrow(() -> new NotFoundProviderException(providerName));
    }
}
