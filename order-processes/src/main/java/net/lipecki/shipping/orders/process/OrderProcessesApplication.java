package net.lipecki.shipping.orders.process;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.commandhandling.gateway.DefaultCommandGateway;
import org.axonframework.commandhandling.gateway.IntervalRetryScheduler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.Executors;

@SpringBootApplication
public class OrderProcessesApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderProcessesApplication.class, args);
    }

    @Bean
    public CommandGateway commandGateway(CommandBus commandBus) {
        return DefaultCommandGateway.builder()
                .commandBus(commandBus)
                .retryScheduler(
                        IntervalRetryScheduler.builder()
                                .maxRetryCount(300)
                                .retryInterval(30000)
                                .retryExecutor(Executors.newScheduledThreadPool(10))
                                .build()
                )
                .build();
    }

}

