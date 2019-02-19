package net.lipecki.shipping.orders.list;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.lipecki.shipping.orders.ShippingOrderAcceptedEvent;
import net.lipecki.shipping.orders.ShippingOrderCreatedEvent;
import net.lipecki.shipping.orders.ShippingOrderRejectedEvent;
import net.lipecki.shipping.orders.ShippingOrderSentEvent;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class OrderEventsHandler {

    private OrdersRepository repository;

    @EventHandler
    public void on(final ShippingOrderCreatedEvent event) {
        log.debug("Update projection [event={}]", event);
        repository.save(
                OrderListEntity.builder()
                        .id(event.getId())
                        .status(OrderStatus.DRAFT)
                        .build()
        );
    }

    @EventHandler
    public void on(final ShippingOrderSentEvent event) {
        log.debug("Update projection [event={}]", event);
        updateRow(event.getId(), OrderStatus.SENT);
    }

    @EventHandler
    public void on(final ShippingOrderRejectedEvent event) {
        log.debug("Update projection [event={}]", event);
        updateRow(event.getId(), OrderStatus.REJECTED);
    }

    @EventHandler
    public void on(final ShippingOrderAcceptedEvent event) {
        log.debug("Update projection [event={}]", event);
        updateRow(event.getId(), OrderStatus.ACCEPTED);
    }

    private void updateRow(String id, OrderStatus sent) {
        final OrderListEntity entity = repository.findById(id).orElseThrow(IllegalArgumentException::new);
        entity.setStatus(sent);
        repository.save(entity);
    }

}
