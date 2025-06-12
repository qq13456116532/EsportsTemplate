package com.esports.esports.repository;

import com.esports.esports.model.PlayerOrder;
import com.esports.esports.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PlayerOrderRepository extends JpaRepository<PlayerOrder, Long> {
    // Find orders by user ID
    List<PlayerOrder> findByUserId(Long userId);
    
    // Find orders by user ID and a specific status
    List<PlayerOrder> findByUserIdAndStatus(Long userId, OrderStatus status);
}