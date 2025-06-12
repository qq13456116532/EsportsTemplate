package com.esports.esports.service;

import com.esports.esports.model.User;
import com.esports.esports.repository.UserRepository;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final StatefulRedisConnection<String, String> redis;
    private final UserRepository userRepo;

    /**
     * 从 Authorization 头取出 Bearer token → openid → userId
     */
    public Long currentUserId(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer "))
            throw unauth("缺少或非法 Authorization 头");

        String token = authHeader.substring(7).trim();
        RedisCommands<String, String> cmd = redis.sync();
        String openid = cmd.get("login_token:" + token);
        if (openid == null)
            throw unauth("token 已过期，请重新登录");

        User user = userRepo.findByOpenId(openid)
                            .orElseThrow(() -> unauth("用户不存在，请先注册"));
        return user.getId();
    }

    private ResponseStatusException unauth(String msg) {
        return new ResponseStatusException(HttpStatus.UNAUTHORIZED, msg);
    }
}
