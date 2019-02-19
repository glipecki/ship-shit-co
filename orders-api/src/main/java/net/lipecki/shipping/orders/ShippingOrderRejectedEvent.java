package net.lipecki.shipping.orders;

import lombok.Value;

@Value(staticConstructor = "of")
public class ShippingOrderRejectedEvent {

    private final String id;

}
