package com.skapral.parrot.auth.ops;

import com.skapral.parrot.auth.data.Role;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.UUID;

public class CreateUserIfDoesntExist extends DoIfUserDoesntExist {
    public CreateUserIfDoesntExist(JdbcTemplate jdbcTemplate, RabbitTemplate rabbitTemplate, UUID id, String login, Role role) {
        super(
                jdbcTemplate,
                id,
                login,
                new SequentialOperation(
                    new NewUser(jdbcTemplate, id, login, role),
                    new SendUserMessage(rabbitTemplate, id, login, role)
                )
        );
    }
}
