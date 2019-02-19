package net.lipecki.shipping.orders;

import lombok.Value;

@Value(staticConstructor = "of")
public class ItemAddedToShippingOrderEvent {

    private final String id;
    private final Item item;

}
