package com.skapral.parrot.auth.ops;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skapral.parrot.common.DoAndNotify;
import com.skapral.parrot.common.events.EventType;
import com.skapral.parrot.common.events.data.User;
import com.skapral.parrot.common.events.impl.RabbitEvent;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.UUID;

public class CreateUserIfDoesntExist extends DoIfUserDoesntExist {
    public CreateUserIfDoesntExist(JdbcTemplate jdbcTemplate, ObjectMapper mapper, UUID id, String login, String role) {
        super(
                jdbcTemplate,
                id,
                login,
                new DoAndNotify(
                    new NewUser(jdbcTemplate, id, login, role),
                    new RabbitEvent<>(jdbcTemplate, mapper, "outbox", "", EventType.USER_NEW, new User(id, login, role))
                )
        );
    }
}
