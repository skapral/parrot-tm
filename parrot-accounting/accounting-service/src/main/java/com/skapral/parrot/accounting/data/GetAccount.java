package com.skapral.parrot.accounting.data;

import com.skapral.parrot.accounting.rest.Account;
import com.skapral.parrot.common.Query;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.UUID;

public class GetAccount implements Query<Account> {
    private final JdbcTemplate template;
    private final UUID accountId;

    public GetAccount(JdbcTemplate template, UUID accountId) {
        this.template = template;
        this.accountId = accountId;
    }

    @Override
    public final Account get() {
        return template.queryForObject(
            "SELECT id, money FROM account WHERE id = ?",
            new AccountRowMapper()
        );
    }
}
