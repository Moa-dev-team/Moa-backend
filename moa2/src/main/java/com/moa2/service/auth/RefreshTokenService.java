package com.moa2.service.auth;

import com.moa2.repository.redis.RedisRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenService {
    private final RedisRepository redisRepository;

    private final long refreshTokenValidityInSeconds;

    public RefreshTokenService(RedisRepository redisRepository,
                               @Value("${app.jwt.refresh-token-validity-in-seconds}")
                               long refreshTokenValidityInSeconds) {
        this.redisRepository = redisRepository;
        this.refreshTokenValidityInSeconds = refreshTokenValidityInSeconds;
    }

    private String getKey(String token) {
        return "refresh_token:" + token;
    }

    public void setRefreshTokenWithAccessToken(String refreshToken, String accessToken) {
        redisRepository.setWithTimeout(getKey(refreshToken), accessToken,
                refreshTokenValidityInSeconds);
    }

    public String getRefreshToken(String refreshToken) {
        return redisRepository.get(getKey(refreshToken));
    }

    public void deleteRefreshToken(String refreshToken) {
        redisRepository.delete(getKey(refreshToken));
    }

    public boolean isExistRefreshToken(String refreshToken) {
        return redisRepository.get(getKey(refreshToken)) != null;
    }

}
