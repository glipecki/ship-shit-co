package net.liepcki.shipping.payments;

import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Value(staticConstructor = "of")
public class CreatePaymentCommand {

    @TargetAggregateIdentifier
    private String id;

}
