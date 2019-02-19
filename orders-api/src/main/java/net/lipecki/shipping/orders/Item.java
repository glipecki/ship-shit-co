package net.lipecki.shipping.orders;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class Item {

    private BigDecimal weight;
    private BigDecimal value;

}
