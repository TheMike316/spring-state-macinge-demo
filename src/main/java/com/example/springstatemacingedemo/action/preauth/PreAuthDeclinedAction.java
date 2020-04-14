package com.example.springstatemacingedemo.action.preauth;

import com.example.springstatemacingedemo.domain.PaymentEvent;
import com.example.springstatemacingedemo.domain.PaymentState;
import com.example.springstatemacingedemo.service.PaymentServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PreAuthDeclinedAction implements Action<PaymentState, PaymentEvent> {
    @Override
    public void execute(StateContext<PaymentState, PaymentEvent> context) {
        // for demo purposes. this could send a message or make an api call
        log.info("PreAuth for Payment {} was declined", context.getMessageHeaders().get(PaymentServiceImpl.PAYMENT_ID_HEADER));

    }
}
