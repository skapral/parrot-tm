package com.skapral.parrot.accounting.rest;

import com.skapral.parrot.accounting.data.GetTransactionLog;
import io.vavr.collection.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.UUID;

@RestController
public class TransactionLogRest {
    private final JdbcTemplate jdbcTemplate;

    public TransactionLogRest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping
    public List<TransactionLog> current() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var id = UUID.fromString(auth.getPrincipal().toString());
        return new GetTransactionLog(
            jdbcTemplate,
            id,
            Duration.ofDays(1)
        ).get();
    }
}
