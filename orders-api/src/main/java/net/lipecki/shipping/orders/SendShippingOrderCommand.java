package net.lipecki.shipping.orders;

import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Value(staticConstructor = "of")
public class SendShippingOrderCommand {

    @TargetAggregateIdentifier
    private String id;

}
