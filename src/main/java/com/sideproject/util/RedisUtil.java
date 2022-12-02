package com.sideproject.util;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisUtil {

    /**
     * [Spring] 이메일 인증 번호 전송, 유효 시간 (Gmail SMTP Server, Redis)
     * dmaolon00.tistory.com/109
     */
    private final RedisTemplate<String, String> redisTemplate;

    // key를 통해 value 리턴
    public String getData(String key) {
        // opsForValue() : 단순 값에 대해 수행된 작업을 반환. ValueOperations<K, V>
        val valueOperations = redisTemplate.opsForValue();
        return valueOperations.get(key);
    }

    public void setData(String key, String value) {
        val valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value);
    }

    /**
     * 유효 시간 동안 (key, value) 저장
     * Ex) 3분, setDataExpire(key, value, 60 * 3L);
     */
    public void setDataExpire(String key, String value, long duration) {
        val valueOperations = redisTemplate.opsForValue();
        Duration expireDuration = Duration.ofSeconds(duration);
        valueOperations.set(key, value, expireDuration);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }
}
