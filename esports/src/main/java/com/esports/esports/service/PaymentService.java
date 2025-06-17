package com.esports.esports.service;

import com.esports.esports.model.*;
import com.esports.esports.repository.PaymentRecordRepository;
import com.esports.esports.repository.PlayerOrderRepository;
import com.esports.esports.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PlayerOrderRepository orderRepo;
    private final PaymentRecordRepository payRepo;
    private final ProductRepository productRepository;
    /** 模拟付款：将订单状态置为 ONGOING，并记录 PaymentRecord */
    @Transactional
    public PlayerOrder pay(Long orderId) {
        PlayerOrder order = orderRepo.findById(orderId)
                                     .orElseThrow(() -> new RuntimeException("订单不存在"));
        if (order.getStatus() != OrderStatus.PENDING_PAYMENT) {
            throw new RuntimeException("当前订单状态无法支付");
        }

        // 1) 更新订单状态
        order.setStatus(OrderStatus.ONGOING);
        orderRepo.save(order);
        // 2) 更新产品售出数量    
        Product product = order.getProduct();
        product.setSales(product.getSales() + order.getQuantity());
        productRepository.save(product);

        // 3) 写入支付记录
        PaymentRecord pr = new PaymentRecord();
        pr.setOrder(order);
        pr.setAmount(order.getProduct().getPrice().toPlainString());
        payRepo.save(pr);

        return order;
    }

    /** 模拟退款：将订单状态置为 REFUNDED，并更新 PaymentRecord */
    @Transactional
    public PlayerOrder refund(Long orderId) {
        PlayerOrder order = orderRepo.findById(orderId)
                                     .orElseThrow(() -> new RuntimeException("订单不存在"));
        if (order.getStatus() != OrderStatus.ONGOING) {
            throw new RuntimeException("仅已支付订单可退款");
        }

        order.setStatus(OrderStatus.REFUNDED);
        orderRepo.save(order);

        PaymentRecord pr = payRepo.findByOrderId(orderId)
                                  .orElseThrow(() -> new RuntimeException("支付记录缺失"));
        pr.setStatus("REFUNDED");
        pr.setRefundedAt(java.time.LocalDateTime.now());
        payRepo.save(pr);

        return order;
    }
}
