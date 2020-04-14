package com.example.springstatemacingedemo.action.preauth;

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
public class PreAuthAction implements Action<PaymentState, PaymentEvent> {
    @Override
    public void execute(StateContext<PaymentState, PaymentEvent> context) {
        log.info("PreAuth was called");

        // randomly approve/decline pre-auth (~80% approve, 20% decline)
        if (new Random().nextInt(10) < 8) {
            log.info("PreAuth was approved");
            var message = MessageBuilder.withPayload(PaymentEvent.PRE_AUTH_APPROVED)
                    .copyHeaders(context.getMessageHeaders())
                    .build();
            context.getStateMachine().sendEvent(message);
        } else {
            log.warn("PreAuth was declined!");
            var message = MessageBuilder.withPayload(PaymentEvent.PRE_AUTH_DECLINED)
                    .copyHeaders(context.getMessageHeaders())
                    .build();
            context.getStateMachine().sendEvent(message);
        }
    }
}
