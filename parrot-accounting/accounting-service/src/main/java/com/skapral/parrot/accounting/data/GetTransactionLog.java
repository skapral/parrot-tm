package com.skapral.parrot.accounting.data;

import com.skapral.parrot.accounting.rest.TransactionLog;
import com.skapral.parrot.common.Query;
import io.vavr.collection.List;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Time;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

public class GetTransactionLog implements Query<List<TransactionLog>> {
    private final JdbcTemplate template;
    private final UUID accountId;
    private final Duration timePeriod;

    public GetTransactionLog(JdbcTemplate template, UUID accountId, Duration timePeriod) {
        this.template = template;
        this.accountId = accountId;
        this.timePeriod = timePeriod;
    }

    @Override
    public final List<TransactionLog> get() {
        var now = LocalDateTime.now();
        var then = now.minus(timePeriod);
        return List.ofAll(
            template.query(
                "SELECT id, time, accountid, description, debit, credit FROM transactionlog WHERE accountid = ? AND time BETWEEN ? AND ?",
                new TransactionLogRowMapper(),
                accountId,
                now,
                then
            )
        );
    }
}
