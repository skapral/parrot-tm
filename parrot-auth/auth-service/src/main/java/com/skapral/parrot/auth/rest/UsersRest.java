package com.skapral.parrot.auth.rest;

import com.skapral.parrot.auth.queries.Users;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UsersRest {
    private final JdbcTemplate jdbcTemplate;

    public UsersRest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping
    public Iterable<User> getUsers() {
        return new Users(jdbcTemplate).get();
    }
}
