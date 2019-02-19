package net.lipecki.shipping.orders;

import lombok.Value;

@Value(staticConstructor = "of")
public class ShippingOrderSentEvent {

    private final String id;

}
