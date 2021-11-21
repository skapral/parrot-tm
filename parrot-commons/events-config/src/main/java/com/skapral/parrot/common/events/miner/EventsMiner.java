package com.skapral.parrot.common.events.miner;

import io.vavr.collection.List;
import io.vavr.control.Option;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Slf4j
public class EventsMiner {
    private final JdbcTemplate template;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public EventsMiner(JdbcTemplate template, RabbitTemplate rabbitTemplate) {
        this.template = template;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Scheduled(fixedRate = 5000)
    @Transactional
    public void mine() {
        var unsentEvents = List.ofAll(
            template.query("SELECT * FROM outbox WHERE sent = false", new EventRowMapper())
        );
        if(unsentEvents.isEmpty()) {
            return;
        }
        log.info("MINER");
        var sentEvents = unsentEvents
            .flatMap(
                event -> {
                    try {
                        var props = new MessageProperties();
                        props.setMessageId(event.getUuid().toString());
                        props.setType(event.getType());
                        props.setContentType("application/json");
                        var msg = new Message(
                            event.getPayload().getBytes(),
                            props
                        );
                        rabbitTemplate.send(
                            event.getOutbox(),
                            event.getRoutingKey(),
                            msg
                        );
                        return Option.of(event);
                    } catch (AmqpException ex) {
                        log.warn("Unable to send message " + event.getUuid(), ex);
                        return Option.none();
                    }
                }
            )
            .map(Event::getUuid);
        log.info("Events sent: " + sentEvents.map(Object::toString).collect(Collectors.joining(", ")));
        template.batchUpdate(
            "UPDATE outbox SET sent = true WHERE id = ?",
            sentEvents.map(o -> new Object[] {o}).asJava()
        );
    }
}
