package com.example.springstatemacingedemo.config;

import com.example.springstatemacingedemo.domain.PaymentEvent;
import com.example.springstatemacingedemo.domain.PaymentState;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.config.StateMachineFactory;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class StateMachineConfigTest {

    @Autowired
    StateMachineFactory<PaymentState, PaymentEvent> factory;

    @Test
    void testStateMachine() {
        var machine = factory.getStateMachine(UUID.randomUUID());

        machine.start();

        System.out.println(machine.getState());
        assertEquals(PaymentState.NEW, machine.getState().getId());

        machine.sendEvent(PaymentEvent.PRE_AUTHORIZE);

        System.out.println(machine.getState());
        assertEquals(PaymentState.NEW, machine.getState().getId());

        machine.sendEvent(PaymentEvent.PRE_AUTH_APPROVED);

        System.out.println(machine.getState());
        assertEquals(PaymentState.PRE_AUTH, machine.getState().getId());

        machine.sendEvent(PaymentEvent.AUTHORIZE);

        System.out.println(machine.getState());
        assertEquals(PaymentState.PRE_AUTH, machine.getState().getId());

        machine.sendEvent(PaymentEvent.AUTH_APPROVED);

        System.out.println(machine.getState());
        assertEquals(PaymentState.AUTH, machine.getState().getId());
    }
}