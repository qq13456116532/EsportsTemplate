package com.esports.esports.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
public class PaymentRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 关联订单 */
    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    private PlayerOrder order;

    /** 支付渠道（这里固定 MOCK） */
    private String channel = "MOCK";

    /** 支付金额，冗余字段，方便后期对账 */
    private String amount;

    /** 支付状态：SUCCESS / REFUNDED / FAIL（可酌情扩展） */
    private String status = "SUCCESS";

    private LocalDateTime paidAt;
    private LocalDateTime refundedAt;

    @PrePersist
    void onCreate() {
        paidAt = LocalDateTime.now();
    }
}
