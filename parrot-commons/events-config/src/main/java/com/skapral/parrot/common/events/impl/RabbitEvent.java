package com.skapral.parrot.common.events.impl;

import com.skapral.parrot.common.Event;
import com.skapral.parrot.common.events.EventType;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.Optional;
import java.util.UUID;

public class RabbitEvent<T> implements Event {
    private final RabbitTemplate template;
    private final String exchange;
    private final String routingKey;
    private final EventType type;
    private final T payload;

    public RabbitEvent(RabbitTemplate template, String exchange, String routingKey, EventType type, T payload) {
        this.template = template;
        this.exchange = exchange;
        this.routingKey = routingKey;
        this.type = type;
        this.payload = payload;
    }

    @Override
    public final void send() {
        Class<?> clazz = null;
        if(payload instanceof Iterable<?>) {
            var iter = ((Iterable<?>) payload).iterator();
            if(iter.hasNext()) {
                clazz = iter.next().getClass();
            }
        }
        var _c = clazz;
        template.convertAndSend(
                exchange,
                routingKey,
                payload,
                message -> {
                    var props = message.getMessageProperties();
                    props.setMessageId(UUID.randomUUID().toString());
                    props.setType(type.name());
                    // Nasty hack to overcome generics erasion
                    Optional.ofNullable(_c).ifPresent(c -> props.setHeader("__ContentTypeId__", c.getName()));
                    return message;
                }
        );
    }
}
