package com.esports.esports.model;

import lombok.Data;

@Data
public class  WxUserInfoDTO{
    private String nickName;
    private String avatarUrl;
    private Integer gender; // 0: 未知, 1: 男, 2: 女
    private String province;
    private String city;
    private String country;
}