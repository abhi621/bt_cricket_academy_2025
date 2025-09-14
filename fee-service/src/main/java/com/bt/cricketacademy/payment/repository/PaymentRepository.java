package com.bt.cricketacademy.payment.repository;

import com.bt.cricketacademy.payment.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
