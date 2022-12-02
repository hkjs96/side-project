package com.sideproject.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Spring boot + Redis Docker : 오리엔탈 킴 2022.4.14
 * kim-oriental.tistory.com/30
 *
 * Redis docker-compose 실행하기
 * https://www.woolog.dev/backend/docker/database-with-docker/
 */
@Configuration
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(host, port);
    }

    @Bean
    public RedisTemplate<String, Integer> redisTemplate() {
        // RedisTemplate : Redis 저장소에서 지정된 개채와 binary 데이터간의 Auto 직렬화/역직렬화 제공
        // 스레드로 부터 안전
        RedisTemplate<String, Integer> redisTemplate = new RedisTemplate<>();
        //  Redis 에서 사용할 직렬화 키/값 설정
        // StringRedisSerializer 는 String 간 서로 컨버팅 byte[], 빈 문자열일 때 Null 반환 없음.
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactory());

        return redisTemplate;
    }
}
