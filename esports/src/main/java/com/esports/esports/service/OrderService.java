package com.esports.esports.service;

import com.esports.esports.model.PlayerOrder;
import com.esports.esports.model.OrderStatus;
import com.esports.esports.repository.PlayerOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    
    private final PlayerOrderRepository orderRepository;

    public List<PlayerOrder> getOrdersForUser(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    public List<PlayerOrder> getOrdersForUserByStatus(Long userId, OrderStatus status) {
        return orderRepository.findByUserIdAndStatus(userId, status);
    }
}