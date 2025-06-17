package com.esports.esports.controller;

import com.esports.esports.model.PlayerOrder;
import com.esports.esports.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pay")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    /** 模拟支付 */
    @PostMapping("/{orderId}")
    public ResponseEntity<PlayerOrder> pay(@PathVariable Long orderId) {
        return ResponseEntity.ok(paymentService.pay(orderId));
    }

    /** 模拟退款 */
    @PostMapping("/{orderId}/refund")
    public ResponseEntity<PlayerOrder> refund(@PathVariable Long orderId) {
        return ResponseEntity.ok(paymentService.refund(orderId));
    }
}
