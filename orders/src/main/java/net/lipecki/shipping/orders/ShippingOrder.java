package net.lipecki.shipping.orders;

import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import java.math.BigDecimal;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Slf4j
@ToString
@Aggregate
@NoArgsConstructor
class ShippingOrder {

    private enum Status {
        DRAFT,
        PENDING,
        ACCEPTED,
        DECLINED
    }

    private static final BigDecimal maxWeightLimit = new BigDecimal(1000);
    private static final BigDecimal discountEligibleValue = new BigDecimal(5000);
    private static final BigDecimal discountPercent = new BigDecimal(10);

    @AggregateIdentifier
    private String id;
    private BigDecimal totalWeight = BigDecimal.ZERO;
    private BigDecimal totalValue = BigDecimal.ZERO;
    private Status status = Status.DRAFT;
    private int itemCount = 0;

    @CommandHandler
    ShippingOrder(final CreateShippingOrderCommand cmd) {
        apply(ShippingOrderCreatedEvent.of(cmd.getId()));
    }

    @CommandHandler
    void handle(final AddItemToShippingOrderCommand cmd) {
        if (status != Status.DRAFT) {
            throw new IllegalStateException(String.format("Order cannot be changed after sent [status=%s", status));
        }
        final BigDecimal newTotalWeight = totalWeight.add(cmd.getItem().getWeight());
        if (newTotalWeight.compareTo(maxWeightLimit) >= 0) {
            throw new IllegalStateException(String.format("Add item rejected due to max weight limit exceed [newTotalWeight=%s]", newTotalWeight.toString()));
        }

        apply(ItemAddedToShippingOrderEvent.of(id, cmd.getItem()));
        if (totalValue.add(cmd.getItem().getValue()).compareTo(discountEligibleValue) >= 0) {
            apply(ShippingOrderDiscountAppliedEvent.of(id, discountPercent));
        }
    }

    @CommandHandler
    void handle(final RemoveItemFromShippingOrderCommand cmd) {
        if (status != Status.DRAFT) {
            throw new IllegalStateException(String.format("Removing rejected due to wrong order status [status=%s]", status));
        }

        apply(ItemRemovedFromShippingOrderEvent.of(id, cmd.getItem()));
    }

    @CommandHandler
    void handle(final SendShippingOrderCommand cmd) {
        if (status != Status.DRAFT) {
            throw new IllegalStateException(String.format("Send rejected due to wrong order status [status=%s]", status));
        }
        if (itemCount == 0) {
            throw new IllegalStateException("Send rejected due to empty order");
        }

        apply(ShippingOrderSentEvent.of(id));
    }

    @CommandHandler
    void handle(final AcceptShippingOrderCommand cmd) {
        apply(ShippingOrderAcceptedEvent.of(cmd.getId()));
    }

    @CommandHandler
    void handle(final RejectShippingOrderCommand cmd) {
        apply(ShippingOrderRejectedEvent.of(cmd.getId()));
    }

    @EventSourcingHandler
    void on(final ShippingOrderCreatedEvent event) {
        id = event.getId();
    }

    @EventSourcingHandler
    void on(final ItemAddedToShippingOrderEvent event) {
        totalValue = event.getItem().getValue();
        totalWeight = event.getItem().getWeight();
        itemCount = itemCount + 1;
    }

    @EventSourcingHandler
    void on(final ItemRemovedFromShippingOrderEvent event) {
        totalValue = event.getItem().getValue();
        totalWeight = event.getItem().getWeight();
        itemCount = itemCount - 1;
    }

    @EventSourcingHandler
    void on(final ShippingOrderSentEvent event) {
        status = Status.PENDING;
    }

}
