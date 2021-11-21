package com.skapral.parrot.common.events.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skapral.parrot.common.Event;
import com.skapral.parrot.common.events.EventType;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Optional;
import java.util.UUID;

public class RabbitEvent<T> implements Event {
    private final JdbcTemplate template;
    private final ObjectMapper objectMapper;
    private final String exchange;
    private final String routingKey;
    private final EventType type;
    private final T payload;

    public RabbitEvent(JdbcTemplate template, ObjectMapper objectMapper, String exchange, String routingKey, EventType type, T payload) {
        this.template = template;
        this.objectMapper = objectMapper;
        this.exchange = exchange;
        this.routingKey = routingKey;
        this.type = type;
        this.payload = payload;
    }

    @Override
    public final void send() {
        try {
            String payloadStr = objectMapper.writeValueAsString(payload);
            template.update("INSERT INTO outbox (id, type, outbox, routingKey, payload) VALUES (?, ?, ?, ?, ?)",
                UUID.randomUUID(),
                type.name(),
                exchange,
                routingKey,
                payloadStr
            );
        } catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
