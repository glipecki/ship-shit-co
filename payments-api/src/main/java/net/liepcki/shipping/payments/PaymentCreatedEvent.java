package net.liepcki.shipping.payments;

import lombok.Value;

@Value(staticConstructor = "of")
public class PaymentCreatedEvent {

    private final String id;

}
