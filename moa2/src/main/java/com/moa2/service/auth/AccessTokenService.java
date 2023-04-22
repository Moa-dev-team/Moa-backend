package com.moa2.service.auth;

import com.moa2.repository.redis.RedisRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AccessTokenService {
    private final RedisRepository redisRepository;
    private final long accessTokenValidityInSeconds;

    public AccessTokenService(RedisRepository redisRepository,
                              @Value("${jwt.access-token-validity-in-seconds}")
                              long accessTokenValidityInSeconds) {
        this.redisRepository = redisRepository;
        this.accessTokenValidityInSeconds = accessTokenValidityInSeconds;
    }

    private String getKey(String token) {
        return "access_token:" + token;
    }

    public void setAccessTokenWithRefreshToken(String accessToken, String refreshToken) {
        redisRepository.setWithTimeout(getKey(accessToken), refreshToken,
                accessTokenValidityInSeconds);
    }

    public String getAccessToken(String accessToken) {
        return redisRepository.get(getKey(accessToken));
    }

    public void deleteAccessToken(String accessToken) {
        redisRepository.delete(getKey(accessToken));
    }
    public boolean isExistAccessToken(String accessToken) {
        return redisRepository.get(getKey(accessToken)) != null;
    }
}
