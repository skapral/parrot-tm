package com.skapral.parrot.auth.ops;

import com.skapral.parrot.auth.data.Role;
import com.skapral.parrot.auth.data.User;
import com.skapral.parrot.common.events.Events;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.UUID;

public class SendUserMessage implements Operation {
    private final RabbitTemplate rabbitTemplate;
    private final UUID id;
    private final String login;
    private final Role role;

    public SendUserMessage(RabbitTemplate rabbitTemplate, UUID id, String login, Role role) {
        this.rabbitTemplate = rabbitTemplate;
        this.id = id;
        this.login = login;
        this.role = role;
    }

    @Override
    public final void execute() {
        rabbitTemplate.convertAndSend(
                "handshake",
                Events.USER.name(),
                new User(
                        id,
                        login,
                        role
                ),
                new CorrelationData(id.toString())
        );
    }
}
