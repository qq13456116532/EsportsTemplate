package com.esports.esports.controller;

import com.esports.esports.model.User;
import com.esports.esports.repository.UserRepository;
import com.esports.esports.service.AuthService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserProfileController {

    private final UserRepository userRepo;
    private final AuthService    authService;   // ← 新增

    /** 修改头像 / 昵称 */
    @PutMapping("/me")
    public ResponseEntity<User> updateProfile(@RequestHeader("Authorization") String authHeader,
                                              @RequestBody UpdateReq body) {

        Long uid = authService.currentUserId(authHeader);
        User user = userRepo.findById(uid)
                            .orElseThrow(() -> new RuntimeException("User not found"));

        if (StringUtils.hasText(body.nickName()))
            user.setNickName(body.nickName().trim());
        if (StringUtils.hasText(body.avatarUrl()))
            user.setAvatarUrl(body.avatarUrl().trim());

        return ResponseEntity.ok(userRepo.save(user));
    }
    /** 简单 DTO，只接收要改的字段 */
    public record UpdateReq(String nickName, String avatarUrl) {}
}
