package com.skapral.parrot.auth.ops;

import com.skapral.parrot.common.Operation;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.UUID;

public class NewUser implements Operation {
    private final JdbcTemplate jdbcTemplate;
    private final UUID id;
    private final String login;
    private final String role;

    public NewUser(JdbcTemplate jdbcTemplate, UUID id, String login, String role) {
        this.jdbcTemplate = jdbcTemplate;
        this.id = id;
        this.login = login;
        this.role = role;
    }

    @Override
    public final void execute() {
        jdbcTemplate.update("INSERT INTO \"user\" (id, login, role) VALUES(?, ?, ?)", id, login, role.toString());
    }
}
