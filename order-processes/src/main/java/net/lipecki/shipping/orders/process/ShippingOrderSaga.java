package net.lipecki.shipping.orders.process;

import lombok.extern.slf4j.Slf4j;
import net.liepcki.shipping.payments.CreatePaymentCommand;
import net.liepcki.shipping.payments.PaymentAcceptedEvent;
import net.liepcki.shipping.payments.PaymentRejectedEvent;
import net.lipecki.shipping.orders.AcceptShippingOrderCommand;
import net.lipecki.shipping.orders.RejectShippingOrderCommand;
import net.lipecki.shipping.orders.ShippingOrderSentEvent;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.SagaLifecycle;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Saga
@Slf4j
public class ShippingOrderSaga {

    @Autowired
    private transient CommandGateway commandGateway;

    private String orderId;

    @StartSaga
    @SagaEventHandler(associationProperty = "id")
    void on(final ShippingOrderSentEvent event) {
        log.info("[event={}]", event);
        orderId = event.getId();

        final String paymentId = UUID.randomUUID().toString();
        SagaLifecycle.associateWith("paymentId", paymentId);
        commandGateway.send(CreatePaymentCommand.of(paymentId));
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "id", keyName = "paymentId")
    void on(final PaymentAcceptedEvent event) {
        log.info("[event={}]", event);
        commandGateway.send(AcceptShippingOrderCommand.of(orderId));
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "id", keyName = "paymentId")
    void on(final PaymentRejectedEvent event) {
        log.info("[event={}]", event);
        commandGateway.send(RejectShippingOrderCommand.of(orderId));
    }

}
