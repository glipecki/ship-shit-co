package net.lipecki.shipping.orders;

import lombok.Value;

@Value(staticConstructor = "of")
public class ShippingOrderCreatedEvent {

    private final String id;

}
