package com.moa.sercurity.redis;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RedisService {

  private final RedisTemplate<String, String> redisTemplate;

  @Transactional
  public void setValues(String key, String value) {
    redisTemplate.opsForValue().set(key, value);
  }

  @Transactional
  public void setValuesWithTimeout(String key, String value, long timeout) {
    redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.MICROSECONDS);
  }

  public String getValues(String key) {
    return redisTemplate.opsForValue().get(key);
  }

  @Transactional
  public void deleteValues(String key) {
    redisTemplate.delete(key);
  }
}
