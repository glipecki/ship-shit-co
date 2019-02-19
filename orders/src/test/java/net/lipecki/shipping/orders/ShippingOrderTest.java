package net.lipecki.shipping.orders;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.axonframework.test.matchers.Matchers.*;

@Slf4j
public class ShippingOrderTest {

    private AggregateTestFixture<ShippingOrder> fixture = new AggregateTestFixture<>(ShippingOrder.class);
    private Item.ItemBuilder itemBuilder = Item.builder()
            .value(new BigDecimal(100))
            .weight(new BigDecimal(100));

    @Test
    public void shouldCreateShippingOrder() {
        fixture.when(CreateShippingOrderCommand.of(UUID.randomUUID().toString()))
                .expectSuccessfulHandlerExecution()
                .expectState(aggregate -> {
                    log.info("[aggregate={}]", aggregate);
                    assertThat(aggregate).isNotNull();
                });
    }

    @Test
    public void shouldAddItem() {
        final String id = UUID.randomUUID().toString();
        final Item item = itemBuilder.build();

        fixture.given(ShippingOrderCreatedEvent.of(id))
                .when(AddItemToShippingOrderCommand.of(id, item))
                .expectEvents(ItemAddedToShippingOrderEvent.of(id, item));
    }

    @Test
    public void shouldRemoveItem() {
        final String id = UUID.randomUUID().toString();
        final Item item = itemBuilder.build();

        fixture.given(ShippingOrderCreatedEvent.of(id))
                .when(RemoveItemFromShippingOrderCommand.of(id, item))
                .expectEvents(ItemRemovedFromShippingOrderEvent.of(id, item));
    }

    @Test
    public void shouldRejectOversizeOrder() {
        final String id = UUID.randomUUID().toString();
        final Item item = itemBuilder.weight(new BigDecimal(1000)).build();

        fixture.given(ShippingOrderCreatedEvent.of(id))
                .when(AddItemToShippingOrderCommand.of(id, item))
                .expectException(IllegalStateException.class);
    }

    @Test
    public void shouldApplyDiscount() {
        final String id = UUID.randomUUID().toString();
        final Item item = itemBuilder.value(new BigDecimal(5000)).build();

        fixture.given(ShippingOrderCreatedEvent.of(id))
                .when(AddItemToShippingOrderCommand.of(id, item))
                .expectEventsMatching(listWithAnyOf(messageWithPayload(
                        equalTo(ShippingOrderDiscountAppliedEvent.of(id, new BigDecimal(10)))
                )));
    }

    @Test
    public void shouldSendOrder() {
        final String id = UUID.randomUUID().toString();

        fixture.given(ShippingOrderCreatedEvent.of(id), ItemAddedToShippingOrderEvent.of(id, itemBuilder.build()))
                .when(SendShippingOrderCommand.of(id))
                .expectEventsMatching(listWithAnyOf(messageWithPayload(
                        equalTo(ShippingOrderSentEvent.of(id))
                )));
    }

    @Test
    public void shouldForbidSendEmptyOrder() {
        final String id = UUID.randomUUID().toString();

        fixture.given(ShippingOrderCreatedEvent.of(id))
                .when(SendShippingOrderCommand.of(id))
                .expectException(IllegalStateException.class);
    }

    @Test
    public void shouldForbidChangeOfSentOrder_Adding() {
        final String id = UUID.randomUUID().toString();
        final Item item = itemBuilder.value(new BigDecimal(5000)).build();

        fixture.given(ShippingOrderCreatedEvent.of(id), ShippingOrderSentEvent.of(id))
                .when(AddItemToShippingOrderCommand.of(id, item))
                .expectException(IllegalStateException.class);
    }

    @Test
    public void shouldForbidChangeOfSentOrder_Removing() {
        final String id = UUID.randomUUID().toString();
        final Item item = itemBuilder.value(new BigDecimal(5000)).build();

        fixture.given(ShippingOrderCreatedEvent.of(id), ShippingOrderSentEvent.of(id))
                .when(RemoveItemFromShippingOrderCommand.of(id, item))
                .expectException(IllegalStateException.class);
    }

    @Test
    public void shouldForbidChangeOfSentOrder_Sending() {
        final String id = UUID.randomUUID().toString();

        fixture.given(ShippingOrderCreatedEvent.of(id), ShippingOrderSentEvent.of(id))
                .when(SendShippingOrderCommand.of(id))
                .expectException(IllegalStateException.class);
    }

}
