package com.example.springstatemacingedemo.action.auth;

import com.example.springstatemacingedemo.domain.PaymentEvent;
import com.example.springstatemacingedemo.domain.PaymentState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import java.util.Random;

@Slf4j
@Component
public class AuthAction implements Action<PaymentState, PaymentEvent> {
    @Override
    public void execute(StateContext<PaymentState, PaymentEvent> context) {
        log.info("Auth was called");

        // randomly approve/decline auth (~80% approve, 20% decline)
        if (new Random().nextInt(10) < 8) {
            log.info("Auth was approved");
            var message = MessageBuilder.withPayload(PaymentEvent.AUTH_APPROVED)
                    .copyHeaders(context.getMessageHeaders())
                    .build();
            context.getStateMachine().sendEvent(message);
        } else {
            log.warn("Auth was declined!");
            var message = MessageBuilder.withPayload(PaymentEvent.AUTH_DECLINED)
                    .copyHeaders(context.getMessageHeaders())
                    .build();
            context.getStateMachine().sendEvent(message);
        }
    }
}
