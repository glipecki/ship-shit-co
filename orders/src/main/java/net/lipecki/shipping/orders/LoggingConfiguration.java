package net.lipecki.shipping.orders;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.eventhandling.EventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
class LoggingConfiguration {

    @Autowired
    private CommandBus commandBus;

    @Autowired
    private EventBus eventBus;

    @PostConstruct
    void setUpLogging() {
        final CommandLoggingInterceptor loggingInterceptor = new CommandLoggingInterceptor();
        commandBus.registerHandlerInterceptor(loggingInterceptor);
        eventBus.registerDispatchInterceptor(loggingInterceptor);
    }

}
