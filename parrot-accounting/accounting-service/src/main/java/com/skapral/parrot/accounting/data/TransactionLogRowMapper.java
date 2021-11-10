package com.skapral.parrot.accounting.data;

import com.skapral.parrot.accounting.rest.TransactionLog;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TransactionLogRowMapper implements RowMapper<TransactionLog> {
    @Override
    public final TransactionLog mapRow(ResultSet rs, int rowNum) throws SQLException {
        return null;
    }
}
