package net.liepcki.shipping.payments;

import lombok.Value;

@Value(staticConstructor = "of")
public class PaymentRejectedEvent {

    private final String id;

}
