package com.skapral.parrot.accounting.rest;

import com.skapral.parrot.accounting.data.GetAccount;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class AccountRest {
    private final JdbcTemplate jdbcTemplate;

    public AccountRest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping
    public Account current() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var id = UUID.fromString(auth.getPrincipal().toString());
        return new GetAccount(
            jdbcTemplate,
            id
        ).get();
    }
}
