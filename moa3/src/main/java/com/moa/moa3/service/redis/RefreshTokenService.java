package com.moa.moa3.service.redis;

import com.moa.moa3.repository.redis.RedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    @Value("${jwt.refresh-token-validity-in-seconds}")
    private Long refreshTokenValidityInSeconds;
    private final RedisRepository redisRepository;
    private String getKey(String token) {
        return "refresh_token:" + token;
    }
    public void mapRtToAt(String refreshToken, String accessToken) {
        redisRepository.setWithTimeout(getKey(refreshToken), accessToken,
                refreshTokenValidityInSeconds);
    }
    public String getAt(String refreshToken) {
        return redisRepository.get(getKey(refreshToken));
    }
    public void deleteRt(String refreshToken) {
        redisRepository.delete(getKey(refreshToken));
    }

    public boolean isExist(String refreshToken) {
        return redisRepository.get(getKey(refreshToken)) != null;
    }
}
