package com.example.springstatemacingedemo.service;

import com.example.springstatemacingedemo.domain.Payment;
import com.example.springstatemacingedemo.domain.PaymentEvent;
import com.example.springstatemacingedemo.domain.PaymentState;
import com.example.springstatemacingedemo.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    public static final String PAYMENT_ID_HEADER = "payment_id";
    private final PaymentRepository repository;
    private final StateMachineFactory<PaymentState, PaymentEvent> stateMachineFactory;
    private final PaymentStateChangeInterceptor interceptor;

    @Override
    public Payment newPayment(Payment payment) {
        payment.setState(PaymentState.NEW);
        return repository.save(payment);
    }

    @Override
    @Transactional
    public StateMachine<PaymentState, PaymentEvent> preAuth(Long paymentId) {
        var stateMachine = buildStateMachine(paymentId);

        sendEvent(paymentId, stateMachine, PaymentEvent.PRE_AUTHORIZE);

        return stateMachine;
    }

    @Override
    @Transactional
    public StateMachine<PaymentState, PaymentEvent> authorizePayment(Long paymentId) {
        var stateMachine = buildStateMachine(paymentId);

        sendEvent(paymentId, stateMachine, PaymentEvent.AUTHORIZE);
        return stateMachine;
    }

    @Deprecated
    @Override
    @Transactional
    public StateMachine<PaymentState, PaymentEvent> declineAuth(Long paymentId) {
        var stateMachine = buildStateMachine(paymentId);

        sendEvent(paymentId, stateMachine, PaymentEvent.AUTH_DECLINED);
        return stateMachine;
    }

    private void sendEvent(Long paymentId, StateMachine<PaymentState, PaymentEvent> stateMachine, PaymentEvent event) {
        var message = MessageBuilder.withPayload(event)
                .setHeader(PAYMENT_ID_HEADER, paymentId)
                .build();

        stateMachine.sendEvent(message);
    }

    private StateMachine<PaymentState, PaymentEvent> buildStateMachine(Long paymentId) {
        var payment = repository.getOne(paymentId);

        var stateMachine = stateMachineFactory.getStateMachine(payment.getId().toString());

        stateMachine.stop();


        stateMachine.getStateMachineAccessor()
                .doWithAllRegions(a -> {
                    a.addStateMachineInterceptor(interceptor);
                    a.resetStateMachine(new DefaultStateMachineContext<>(payment.getState(), null, null, null));
                });
        stateMachine.start();

        return stateMachine;
    }
}
