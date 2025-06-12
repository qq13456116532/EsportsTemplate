package com.esports.esports.controller;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.esports.esports.model.LoginRequest;
import com.esports.esports.service.WxLoginService;


@RestController
@RequestMapping("/wxlogin")
public class UserController {

    @Autowired
    private WxLoginService wxLoginService;

    @PostMapping
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        String code = request.getCode();
        if (code == null || code.isEmpty()) {
            return ResponseEntity.badRequest().body("code is required");
        }

        Map<String, Object> map = wxLoginService.loginWithCode(request);

        return ResponseEntity.ok(map);
    }
    
}
