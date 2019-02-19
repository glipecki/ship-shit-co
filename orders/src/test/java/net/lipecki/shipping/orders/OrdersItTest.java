package net.lipecki.shipping.orders;

import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.eventsourcing.eventstore.inmemory.InMemoryEventStorageEngine;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.autoconfigure.exclude=org.axonframework.springboot.autoconfig.AxonServerAutoConfiguration"
})
@Import(OrdersItTest.AxonTestConfiguration.class)
@RunWith(SpringRunner.class)
public abstract class OrdersItTest {

    @Configuration
    static class AxonTestConfiguration {

        @Bean
        public EventStore eventStore() {
            return EmbeddedEventStore.builder()
                    .storageEngine(new InMemoryEventStorageEngine())
                    .build();
        }

    }

}
