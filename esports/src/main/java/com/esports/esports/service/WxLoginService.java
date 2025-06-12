package com.esports.esports.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.esports.esports.model.LoginRequest;
import com.esports.esports.model.User;
import com.esports.esports.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import lombok.Data;
@Service
public class WxLoginService {

    @Value("${wx.appid}")
    private String appId;
    @Value("${wx.secret}")
    private String secret;

    @Autowired
    private StatefulRedisConnection<String, String> redisConnection;
    @Autowired
    private UserRepository userRepository;
    public Map<String, Object> loginWithCode(LoginRequest request) {
        // Step 1: 请求微信换 openid
        RestTemplate restTemplate = new RestTemplate();
        String url = String.format(
            "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
            appId, secret, request.getCode()
        );
        String result = restTemplate.getForObject(url, String.class);
        WxSessionResponse response;
        try {
             response = new ObjectMapper().readValue(result, WxSessionResponse.class);
             // 继续处理 response
        } catch (JsonProcessingException e) {
            throw new RuntimeException("解析微信返回的 JSON 出错", e);
        }

        if (response == null || response.getOpenid() == null) {
            throw new RuntimeException("Failed to get openid from WeChat");
        }

        String openid = response.getOpenid();
        // 查询数据库中是否存在该用户, 不存在则使用默认信息创建新用户
        Optional<User> userOptional = userRepository.findByOpenId(openid);
        User user = userOptional.orElseGet(() -> {
            User newUser = new User();
            newUser.setOpenId(openid);
            newUser.setNickName("电竞大神");
            newUser.setAvatarUrl("/assets/images/icons/defaultUser.svg");
            return userRepository.save(newUser);
        });

        // Step 2: 生成 token
        String token = UUID.randomUUID().toString().replace("-", "");
        // Step 3: 存入 Redis，设置过期时间（单位：秒）
        RedisCommands<String, String> commands = redisConnection.sync();
        commands.setex("login_token:" + token, 600, openid); // 10分钟有效

        Map<String, Object> res = new HashMap<>();
        res.put("token", token);
        res.put("user", user);
        //可选： 支持用户多端登录互踢，可以同时反向存储 openid → token。

        return res;
    }
}

@Data
class WxSessionResponse {
    private String openid;
    private String session_key;
    private String unionid;
    private Integer errcode;
    private String errmsg;
}