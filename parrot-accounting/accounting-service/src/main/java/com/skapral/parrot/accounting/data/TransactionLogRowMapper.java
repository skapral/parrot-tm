package com.skapral.parrot.accounting.data;

import com.skapral.parrot.accounting.rest.TransactionLog;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class TransactionLogRowMapper implements RowMapper<TransactionLog> {
    @Override
    public final TransactionLog mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new TransactionLog(
            UUID.fromString(rs.getString("id")),
            rs.getTimestamp("time"),
            UUID.fromString(rs.getString("accountid")),
            rs.getString("description"),
            rs.getInt("debit"),
            rs.getInt("credit")
        );
    }
}
