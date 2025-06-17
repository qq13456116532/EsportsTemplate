package com.esports.esports.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.esports.esports.model.PaymentRecord;


public interface PaymentRecordRepository extends JpaRepository<PaymentRecord, Long> {
    Optional<PaymentRecord> findByOrderId(Long orderId);
}

