package com.esports.esports.controller;

import java.time.Duration;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.esports.esports.model.LoginRequest;
import com.esports.esports.model.User;
import com.esports.esports.repository.UserRepository;
import com.esports.esports.service.UserService;
import com.esports.esports.service.WxLoginService;
import com.wechat.pay.java.service.weixinpayscanandride.model.UserRepayState;

import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;


@RestController
public class UserController {

    @Autowired
    private WxLoginService wxLoginService;
    @Autowired
    private UserService userService;
    @Autowired
    private StatefulRedisConnection<String, String> redisConnection;

    @PostMapping
    @RequestMapping("/wxlogin")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        String code = request.getCode();
        if (code == null || code.isEmpty()) {
            return ResponseEntity.badRequest().body("code is required");
        }

        Map<String, Object> map = wxLoginService.loginWithCode(request);

        return ResponseEntity.ok(map);
    }
    
    @PostMapping("/send-code")
    public ResponseEntity<?> sendCode(@RequestBody Map<String, String> body) {
        String phone = body.get("phone");
        // 1. 生成验证码
        String code = String.format("%06d", new Random().nextInt(999999));
        // 2. 保存到Redis
        RedisCommands<String, String> commands = redisConnection.sync();
        commands.setex("LOGIN_CODE:" + phone, 300, code);
        // 3. 发送短信 TO DO 
        System.out.println("验证码发送到 " + phone + " : " + code);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginByPhone(@RequestBody Map<String, String> body) {
        String phone = body.get("phone");
        String code = body.get("code");
        String key = "LOGIN_CODE:" + phone;
        RedisCommands<String, String> commands = redisConnection.sync();
        String cachedCode = commands.get(key);
        if (cachedCode == null || !cachedCode.equals(code)) {
            return ResponseEntity.status(401).body("验证码错误或已过期");
        }
        // 4. 查找用户，无则注册
        User user = userService.findOrCreateByPhone(phone);
        // 5. 生成token
        String token = UUID.randomUUID().toString().replace("-", "");
        return ResponseEntity.ok(Map.of("token", token, "user", user));
    }

    
}
