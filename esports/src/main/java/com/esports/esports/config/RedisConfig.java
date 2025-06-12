package com.esports.esports.config;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisConfig {

    @Value("${redis.host}")
    private String host;

    @Value("${redis.port}")
    private int port;

    @Value("${redis.username}")
    private String username;

    @Value("${redis.password}")
    private String password;

    @Bean
    public RedisClient redisClient() {
        RedisURI uri = RedisURI.Builder
            .redis(host, port)
            .withAuthentication(username, password)
            .build();

        return RedisClient.create(uri);
    }

    @Bean
    public StatefulRedisConnection<String, String> redisConnection(RedisClient client) {
        return client.connect();
    }
}
