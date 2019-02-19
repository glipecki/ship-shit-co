package net.lipecki.shipping.orders;

import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Value(staticConstructor = "of")
public class AddItemToShippingOrderCommand {

    @TargetAggregateIdentifier
    private String id;
    private Item item;

}
