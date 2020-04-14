package com.example.springstatemacingedemo.service;

import com.example.springstatemacingedemo.domain.Payment;
import com.example.springstatemacingedemo.domain.PaymentState;
import com.example.springstatemacingedemo.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
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

    Payment payment;

    @BeforeEach
    void setUp() {
        payment = Payment.builder().amount(new BigDecimal("9.99")).build();
    }

    @Transactional
    @RepeatedTest(10)
    void preAuth() {
        var savedPayment = repository.save(payment);
        Long paymentId = savedPayment.getId();

        var sm = service.preAuth(paymentId);

        PaymentState actualState = sm.getState().getId();
        System.out.println(actualState);
        assert PaymentState.PRE_AUTH.equals(actualState) ||
                PaymentState.PRE_AUTH_ERROR.equals(actualState);
    }

    @Transactional
    @RepeatedTest(10)
    void auth() {
        payment.setState(PaymentState.PRE_AUTH);
        var savedPayment = repository.save(payment);
        Long paymentId = savedPayment.getId();

        var sm = service.authorizePayment(paymentId);

        PaymentState actualState = sm.getState().getId();
        System.out.println(actualState);
        assert PaymentState.AUTH.equals(actualState) ||
                PaymentState.AUTH_ERROR.equals(actualState);
    }
}