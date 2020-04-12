package com.example.springstatemacingedemo.repository;

import com.example.springstatemacingedemo.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
