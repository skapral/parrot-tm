package com.skapral.parrot.tasks.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skapral.parrot.common.events.EventType;
import com.skapral.parrot.common.events.EventsConfig;
import com.skapral.parrot.common.events.data.User;
import com.skapral.parrot.tasks.ops.CreateAssignee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@Import(EventsConfig.class)
public class EventsInbox {
    private static final Logger log = LoggerFactory.getLogger(EventsInbox.class);

    private final JdbcTemplate template;
    private final ObjectMapper objectMapper;

    @Autowired
    public EventsInbox(JdbcTemplate template, ObjectMapper objectMapper) {
        this.template = template;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = "${amqp.inbox}")
    @Transactional
    public void listen(Message message) {
        log.info("appId = " + message.getMessageProperties().getAppId());
        log.info("correlationId = " + message.getMessageProperties().getCorrelationId());
        log.info("type = " + message.getMessageProperties().getType());
        var type = EventType.valueOf(message.getMessageProperties().getType());
        try {
            switch (type) {
                case USER_NEW: {
                    var user = objectMapper.readValue(message.getBody(), User.class);
                    new CreateAssignee(template, user.getId(), user.getLogin()).execute();
                    break;
                }
                default:
                    log.warn("Received unrecognized message: " + message);
            }
        } catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
