package com.skapral.parrot.common.events.impl;

import com.skapral.parrot.common.Event;
import com.skapral.parrot.common.events.EventType;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

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
        template.convertAndSend(
                exchange,
                routingKey,
                payload,
                message -> {
                    var props = message.getMessageProperties();
                    props.setMessageId(UUID.randomUUID().toString());
                    props.setType(type.name());
                    return message;
                }
        );
    }
}
