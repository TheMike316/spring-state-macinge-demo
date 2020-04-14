package com.example.springstatemacingedemo.service;

import com.example.springstatemacingedemo.domain.Payment;
import com.example.springstatemacingedemo.repository.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@SpringBootTest
class PaymentServiceImplTest {

    @Autowired
    PaymentService service;

    @Autowired
    PaymentRepository repository;

    Payment payment = Payment.builder().amount(new BigDecimal("9.99")).build();

    @Test
    @Transactional
    void preAuth() throws InterruptedException {
        var savedPayment = repository.save(payment);
        Long paymentId = savedPayment.getId();

        service.preAuth(paymentId);

        Thread.sleep(500);

        var changedPayment = repository.getOne(paymentId);

        System.out.println(changedPayment);
    }
}