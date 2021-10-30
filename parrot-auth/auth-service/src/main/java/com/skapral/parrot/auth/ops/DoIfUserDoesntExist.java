package com.skapral.parrot.auth.ops;


import com.skapral.parrot.common.Operation;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.UUID;

public class DoIfUserDoesntExist implements Operation {
    private final JdbcTemplate jdbcTemplate;
    private final UUID id;
    private final String login;
    private final Operation delegate;

    public DoIfUserDoesntExist(JdbcTemplate jdbcTemplate, UUID id, String login, Operation delegate) {
        this.jdbcTemplate = jdbcTemplate;
        this.id = id;
        this.login = login;
        this.delegate = delegate;
    }

    @Override
    public final void execute() {
        var userExists = jdbcTemplate.queryForObject("SELECT EXISTS(SELECT 1 FROM \"user\" WHERE id = ? OR login = ?)", Boolean.class, id, login);
        if(!userExists) {
            delegate.execute();
        }
    }
}
