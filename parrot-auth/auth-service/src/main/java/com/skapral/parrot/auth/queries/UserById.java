package com.skapral.parrot.auth.queries;

import com.skapral.parrot.auth.rest.User;
import com.skapral.parrot.common.Query;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Optional;
import java.util.UUID;

public class UserById implements Query<Optional<User>> {
    private final JdbcTemplate template;
    private final UUID id;

    public UserById(JdbcTemplate template, UUID id) {
        this.template = template;
        this.id = id;
    }

    @Override
    public final Optional<User> get() {
        return Optional.ofNullable(
                template.queryForObject("SELECT id, login, role FROM \"user\" WHERE id = ?", new UserRowMapper(), id)
        );
    }
}
