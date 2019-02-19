package net.lipecki.shipping.orders;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("orders")
public class OrderController {

    private CommandGateway commandGateway;

    @PostMapping("/")
    public String create() {
        return commandGateway.sendAndWait(CreateShippingOrderCommand.of(UUID.randomUUID().toString()));
    }

    @PostMapping("/{id}/send")
    public String send(@PathVariable final String id) {
        return commandGateway.sendAndWait(SendShippingOrderCommand.of(id));
    }

    @PostMapping("/{id}/add")
    public String addItem(@PathVariable final String id) {
        final Item item = Item.builder()
                .weight(new BigDecimal(100))
                .value(new BigDecimal(100))
                .build();
        return commandGateway.sendAndWait(AddItemToShippingOrderCommand.of(id, item));
    }

}
