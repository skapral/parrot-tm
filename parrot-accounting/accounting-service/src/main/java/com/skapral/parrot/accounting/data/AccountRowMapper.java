package com.skapral.parrot.accounting.data;

import com.skapral.parrot.accounting.rest.Account;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountRowMapper implements RowMapper<Account> {
    @Override
    public final Account mapRow(ResultSet rs, int rowNum) throws SQLException {
        return null;
    }
}
