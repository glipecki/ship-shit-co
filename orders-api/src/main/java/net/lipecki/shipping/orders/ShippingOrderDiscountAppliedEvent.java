package net.lipecki.shipping.orders;

import lombok.Value;

import java.math.BigDecimal;

@Value(staticConstructor = "of")
public class ShippingOrderDiscountAppliedEvent {

    private final String id;
    private final BigDecimal discount;

}
