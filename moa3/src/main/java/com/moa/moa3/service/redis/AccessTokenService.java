package com.moa.moa3.service.redis;

import com.moa.moa3.repository.redis.RedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccessTokenService {
    @Value("${jwt.access-token-validity-in-seconds}")
    private Long accessTokenValidityInSeconds;
    private final RedisRepository redisRepository;

    private String getKey(String token) {
        return "access_token:" + token;
    }

    public void mapAtToRt(String accessToken, String refreshToken) {
        redisRepository.setWithTimeout(getKey(accessToken), refreshToken,
                accessTokenValidityInSeconds);
    }

    public String getRt(String accessToken) {
        return redisRepository.get(getKey(accessToken));
    }

    public void deleteAt(String accessToken) {
        redisRepository.delete(getKey(accessToken));
    }

    public boolean isExist(String accessToken) {
        return redisRepository.get(getKey(accessToken)) != null;
    }
}
