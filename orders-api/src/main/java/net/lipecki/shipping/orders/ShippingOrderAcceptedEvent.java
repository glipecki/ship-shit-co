package net.lipecki.shipping.orders;

import lombok.Value;

@Value(staticConstructor = "of")
public class ShippingOrderAcceptedEvent {

    private final String id;

}
