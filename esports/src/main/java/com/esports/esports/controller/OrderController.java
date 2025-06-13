package com.esports.esports.controller;

import com.esports.esports.model.PlayerOrder;
import com.esports.esports.model.OrderStatus;
import com.esports.esports.service.AuthService;
import com.esports.esports.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final AuthService  authService;  // ← 新增

    @GetMapping
    public ResponseEntity<List<PlayerOrder>> getUserOrders(@RequestHeader("Authorization") String authHeader,
                                                           @RequestParam(required = false) String status) {
        Long uid = authService.currentUserId(authHeader);

        if (status != null && !status.isEmpty()) {
            try {
                OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
                return ResponseEntity.ok(orderService.getOrdersForUserByStatus(uid, orderStatus));
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().build();
            }
        }
        return ResponseEntity.ok(orderService.getOrdersForUser(uid));
    }
    @PostMapping
    public ResponseEntity<PlayerOrder> create(@RequestHeader("Authorization") String authHeader,
                                            @RequestBody CreateReq req) {
        Long uid = authService.currentUserId(authHeader);
        return ResponseEntity.ok(orderService.createOrder(uid, req.productId(), req.quantity(), req.remark()));
    }
    
    public record CreateReq(Long productId, Integer quantity, String remark) {}

}
