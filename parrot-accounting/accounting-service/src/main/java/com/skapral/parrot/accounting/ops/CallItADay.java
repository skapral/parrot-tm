package com.skapral.parrot.accounting.ops;

import com.skapral.parrot.common.Operation;
import io.vavr.collection.List;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.UUID;

public class CallItADay implements Operation {
    private final JdbcTemplate template;

    public CallItADay(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public final void execute() {
        var accounts = List.ofAll(template.queryForList("SELECT id FROM account", UUID.class));
        template.batchUpdate(
                "INSERT INTO transactionlog (id, accountid, description) values (?, ?, ?)",
                accounts.map(accId -> new Object[] {UUID.randomUUID(), accId, "Working day over, time for payback"}).asJava()
        );
    }
}
