package com.skapral.parrot.auth.data;

import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Optional;

public class UserByLogin implements Query<Optional<User>> {
    private final JdbcTemplate template;
    private final String login;

    public UserByLogin(JdbcTemplate template, String login) {
        this.template = template;
        this.login = login;
    }

    @Override
    public final Optional<User> get() {
        return Optional.ofNullable(
            template.queryForObject("SELECT id, login, role FROM \"user\" WHERE login = ?", new UserRowMapper(), login)
        );
    }
}
