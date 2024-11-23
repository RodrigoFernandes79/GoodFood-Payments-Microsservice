package com.goodfood.payments.repository;

import com.goodfood.payments.model.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Page<Payment> findAll(Pageable pageable);
}
