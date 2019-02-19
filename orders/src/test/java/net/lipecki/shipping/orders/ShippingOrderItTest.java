package net.lipecki.shipping.orders;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class ShippingOrderItTest extends OrdersItTest {

    @Autowired
    private CommandGateway commandGateway;

    @Test
    public void shouldCreateShippingOrder() {
        assertThat(commandGateway.<String>sendAndWait(CreateShippingOrderCommand.of(UUID.randomUUID().toString()))).isNotNull();
    }

}

