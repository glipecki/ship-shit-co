package net.lipecki.shipping.orders;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventMessage;
import org.axonframework.eventhandling.GenericDomainEventMessage;
import org.axonframework.messaging.InterceptorChain;
import org.axonframework.messaging.Message;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.axonframework.messaging.MessageHandlerInterceptor;
import org.axonframework.messaging.unitofwork.CurrentUnitOfWork;
import org.axonframework.messaging.unitofwork.UnitOfWork;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

import static org.axonframework.common.ReflectionUtils.fieldsOf;
import static org.axonframework.common.ReflectionUtils.getFieldValue;

@Slf4j
public class CommandLoggingInterceptor<T extends Message<?>> implements MessageHandlerInterceptor<T>, MessageDispatchInterceptor<EventMessage<?>> {

    @Override
    public Object handle(final UnitOfWork<? extends T> unitOfWork, final InterceptorChain interceptorChain)
            throws Exception {
        final T message = unitOfWork.getMessage();
        final String command = message.getPayloadType().getSimpleName();
        final String aggregateId = aggregateId(message).orElse("unknown");

        log.info("{type=commandNew, commandId={}, command={}, aggregateId={}, command={}}", message.getIdentifier(), command, aggregateId, message.getPayload());
        try {
            final Object result = interceptorChain.proceed();
            log.info("{type=commandAccepted, commandId={}, command={}, aggregateId={}, result={}}", message.getIdentifier(), command, aggregateId, result);
            return result;
        } catch (final Exception exception) {
            log.info("{type=commandRejected, commandId={}, command={}, aggregateId={}, rejectionCause={}}", message.getIdentifier(), command, aggregateId, exception.getMessage());
            throw exception;
        }
    }

    @Override
    public BiFunction<Integer, EventMessage<?>, EventMessage<?>> handle(List<? extends EventMessage<?>> messages) {
        CurrentUnitOfWork.ifStarted(unitOfWork -> {
            final String command = unitOfWork.getMessage().getPayloadType().getSimpleName();
            messages.stream()
                    .filter(message -> message instanceof GenericDomainEventMessage)
                    .forEach(message -> {
                        final GenericDomainEventMessage gen = (GenericDomainEventMessage) message;
                        final Object correlationId = message.getMetaData().get("correlationId");
                        final String eventType = gen.getType();
                        final String aggregateIdentifier = gen.getAggregateIdentifier();
                        log.info("{type=event, commandId={}, command={}, aggregateId={}, aggregate={}, event={}}", correlationId, command, aggregateIdentifier, eventType, ((GenericDomainEventMessage) message).getPayload());
                    });
        });
        return (i, m) -> m;
    }

    private Optional<String> aggregateId(T message) {
        for (final Field f : fieldsOf(message.getPayloadType())) {
            if (f.isAnnotationPresent(TargetAggregateIdentifier.class)) {
                return Optional.ofNullable(getFieldValue(f, message.getPayload())).map(Object::toString);
            }
        }
        return Optional.empty();
    }

}
