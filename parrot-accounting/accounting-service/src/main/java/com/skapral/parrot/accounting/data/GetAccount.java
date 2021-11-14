package com.skapral.parrot.accounting.data;

import com.skapral.parrot.accounting.rest.Account;
import com.skapral.parrot.common.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Objects;
import java.util.UUID;

@Slf4j
public class GetAccount implements Query<Account> {
    private final JdbcTemplate template;
    private final UUID accountId;

    public GetAccount(JdbcTemplate template , UUID accountId) {
        this.template = template;
        this.accountId = accountId;
    }

    @Override
    public final Account get() {
        var account = Objects.requireNonNull(
            template.queryForObject(
                "SELECT id, money FROM \"account\" WHERE id = ?",
                new AccountRowMapper(),
                accountId
            )
        );
        return account;
    }
}
