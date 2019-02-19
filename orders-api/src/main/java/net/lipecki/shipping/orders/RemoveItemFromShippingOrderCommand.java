package net.lipecki.shipping.orders;

import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Value(staticConstructor = "of")
public class RemoveItemFromShippingOrderCommand {

    @TargetAggregateIdentifier
    private String id;
    private Item item;

}
