package com.esports.esports.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@Table(name = "app_user") // "user" can be a reserved keyword in some SQL dialects
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true) // WeChat OpenID
    private String openId;

    private String nickName;
    private String avatarUrl;
    private String phone;
}