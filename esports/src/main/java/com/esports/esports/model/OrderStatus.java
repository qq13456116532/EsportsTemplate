package com.esports.esports.model;

public enum OrderStatus {
    PENDING_PAYMENT, // 待付款
    ONGOING,         // 进行中
    PENDING_COMMENT,       // 待评价 (业务上可以认为是已完成)
    REFUNDED,         // 已退款或售后
    COMPLETED
}