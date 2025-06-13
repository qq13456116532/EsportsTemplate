package com.esports.esports.service;

import com.esports.esports.model.PlayerOrder;
import com.esports.esports.model.OrderStatus;
import com.esports.esports.repository.PlayerOrderRepository;
import com.esports.esports.repository.ProductRepository;
import com.esports.esports.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    
    private final PlayerOrderRepository orderRepository;
    
    private final UserRepository userRepository;

    private final ProductRepository productRepository;
    public List<PlayerOrder> getOrdersForUser(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    public List<PlayerOrder> getOrdersForUserByStatus(Long userId, OrderStatus status) {
        return orderRepository.findByUserIdAndStatus(userId, status);
    }
    public PlayerOrder createOrder(Long uid, Long pid, int qty, String remark) {
        PlayerOrder po = new PlayerOrder();
        po.setUser(userRepository.findById(uid).orElseThrow());
        po.setProduct(productRepository.findById(pid).orElseThrow());
        po.setStatus(OrderStatus.PENDING_PAYMENT);
        // 如需数量、备注可在实体上加字段
        return orderRepository.save(po);
    }

}