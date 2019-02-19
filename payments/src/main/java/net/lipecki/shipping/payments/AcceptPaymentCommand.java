package net.lipecki.shipping.payments;

import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Value(staticConstructor = "of")
public class AcceptPaymentCommand {

    @TargetAggregateIdentifier
    private String id;

}
