package com.esports.esports.controller;

import com.esports.esports.model.PlayerOrder;
import com.esports.esports.model.OrderStatus;
import com.esports.esports.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    
    private final OrderService orderService;

    // A placeholder for the user ID. In a real app, this would come from a security context (e.g., JWT).
    private static final Long MOCK_USER_ID = 1L;

    @GetMapping
    public ResponseEntity<List<PlayerOrder>> getUserOrders(@RequestParam(required = false) String status) {
        List<PlayerOrder> orders;
        if (status != null && !status.isEmpty()) {
            try {
                OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
                orders = orderService.getOrdersForUserByStatus(MOCK_USER_ID, orderStatus);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().build(); // Invalid status
            }
        } else {
            orders = orderService.getOrdersForUser(MOCK_USER_ID);
        }
        return ResponseEntity.ok(orders);
    }
}