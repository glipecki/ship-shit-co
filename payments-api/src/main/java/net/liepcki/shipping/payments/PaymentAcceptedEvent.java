package net.liepcki.shipping.payments;

import lombok.Value;

@Value(staticConstructor = "of")
public class PaymentAcceptedEvent {

    private final String id;

}
