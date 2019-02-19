package net.lipecki.shipping.payments;

import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import net.liepcki.shipping.payments.CreatePaymentCommand;
import net.liepcki.shipping.payments.PaymentAcceptedEvent;
import net.liepcki.shipping.payments.PaymentCreatedEvent;
import net.liepcki.shipping.payments.PaymentRejectedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Slf4j
@ToString
@Aggregate
@NoArgsConstructor
class Payment {

    @AggregateIdentifier
    private String id;

    @CommandHandler
    Payment(final CreatePaymentCommand cmd) {
        apply(PaymentCreatedEvent.of(cmd.getId()));
    }

    @CommandHandler
    void handle(final AcceptPaymentCommand cmd) {
        apply(PaymentAcceptedEvent.of(cmd.getId()));
    }

    @CommandHandler
    void handle(final RejectPaymentCommand cmd) {
        apply(PaymentRejectedEvent.of(cmd.getId()));
    }

    @EventSourcingHandler
    void on(final PaymentCreatedEvent event) {
        id = event.getId();
    }

}
