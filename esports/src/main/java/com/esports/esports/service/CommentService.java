package com.esports.esports.service;

import com.esports.esports.controller.CommentController.AddCommentReq;
import com.esports.esports.model.Comment;
import com.esports.esports.model.OrderStatus;
import com.esports.esports.model.PlayerOrder;
import com.esports.esports.model.Product;
import com.esports.esports.model.User;
import com.esports.esports.repository.CommentRepository;
import com.esports.esports.repository.PlayerOrderRepository;
import com.esports.esports.repository.ProductRepository;
import com.esports.esports.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PlayerOrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    /** 查询指定商品的全部评论（按时间倒序） */
    public List<Comment> getCommentsByProduct(Long productId) {
        return commentRepository.findByProductIdOrderByCreatedAtDesc(productId);
    }

    /**
     * 新增评论（订单评价）：
     * 1. 校验订单、用户、商品；
     * 2. 只允许待评价/进行中订单评价，一单只能评一次；
     * 3. 评论成功后可自动将订单状态从 PENDING_COMMENT → REFUNDED/已完成（如有“已完成”状态）。
     */
    public Comment addComment(Long userId, AddCommentReq req) {
        // 1. 查找订单
        PlayerOrder order = orderRepository.findById(req.orderId())
            .orElseThrow(() -> new RuntimeException("订单不存在"));
        if (!order.getUser().getId().equals(userId))
            throw new RuntimeException("只能评价自己的订单");

        // 2. 校验订单状态
        if (!order.getStatus().equals(OrderStatus.PENDING_COMMENT)) {
            throw new RuntimeException("该订单不可评价");
        }

        // 3. 检查是否已评价（你可以在 Comment 表加 unique 约束或业务查一下）
        boolean alreadyCommented = commentRepository.existsByOrderId(req.orderId());
        if (alreadyCommented)
            throw new RuntimeException("该订单已评价");

        // 4. 查找用户、商品
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("用户不存在"));
        Product product = productRepository.findById(req.productId())
            .orElseThrow(() -> new RuntimeException("商品不存在"));

        // 5. 创建评论
        Comment comment = new Comment();
        comment.setOrder(order); // 如果有这个字段
        comment.setProduct(product);
        comment.setUser(user);
        comment.setContent(req.content());
        comment.setRating(req.rating());
        comment.setCreatedAt(LocalDateTime.now());

        // 6. 保存评论
        Comment saved = commentRepository.save(comment);

        // 7. 自动把订单状态从 PENDING_COMMENT → 完成（你如有“已完成”状态则改之，没有可以不动）
        order.setStatus(OrderStatus.COMPLETED);
        orderRepository.save(order);

        return saved;
    }

    // 记得在 CommentRepository 补充 existsByOrderId 方法
}
