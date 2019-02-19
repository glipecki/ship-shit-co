package net.lipecki.shipping.orders;

import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Value(staticConstructor = "of")
public class CreateShippingOrderCommand {

    @TargetAggregateIdentifier
    private String id;

}
