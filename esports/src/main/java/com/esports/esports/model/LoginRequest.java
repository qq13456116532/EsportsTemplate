package com.esports.esports.model;

import lombok.Data;

@Data
public class LoginRequest {
    private String code;              // wx.login 拿到
    private WxUserInfoDTO userInfo;   // 头像、昵称、gender …
}
