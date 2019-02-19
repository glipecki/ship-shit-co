package net.lipecki.shipping.payments;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.liepcki.shipping.payments.PaymentCreatedEvent;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

import java.util.Random;

@Slf4j
@Component
@AllArgsConstructor
class PoorVisaService {

    private final CommandGateway commandGateway;

    @EventHandler
    void handle(final PaymentCreatedEvent event) {
        if (new Random().nextBoolean()) {
            log.info("Payment accepted [id={}]", event.getId());
            commandGateway.send(AcceptPaymentCommand.of(event.getId()));
        } else {
            log.info("Payment rejected [id={}]", event.getId());
            commandGateway.send(RejectPaymentCommand.of(event.getId()));
        }
    }

}
