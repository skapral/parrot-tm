package com.skapral.parrot.accounting.ops;

import com.skapral.parrot.common.Operation;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.UUID;

public class MakeAccount implements Operation {
    private final JdbcTemplate template;
    private final UUID accountId;

    public MakeAccount(JdbcTemplate template, UUID accountId) {
        this.template = template;
        this.accountId = accountId;
    }

    @Override
    public final void execute() {
        template.update("INSERT INTO account (id, value) VALUES (?, ?)", accountId, 0);
    }
}
