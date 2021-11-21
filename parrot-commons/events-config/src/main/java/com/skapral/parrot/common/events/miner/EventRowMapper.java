package com.skapral.parrot.common.events.miner;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class EventRowMapper implements RowMapper<Event> {
    @Override
    public final Event mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Event(
            UUID.fromString(rs.getString("id")),
            rs.getString("type"),
            rs.getString("outbox"),
            rs.getString("routingKey"),
            rs.getString("payload")
        );
    }
}
