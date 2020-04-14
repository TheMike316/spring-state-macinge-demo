package com.example.springstatemacingedemo.service;

import com.example.springstatemacingedemo.domain.Payment;
import com.example.springstatemacingedemo.domain.PaymentState;
import com.example.springstatemacingedemo.repository.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

        var sm = service.preAuth(paymentId);

        assertEquals(PaymentState.PRE_AUTH, sm.getState().getId());
    }
}