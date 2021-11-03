package com.skapral.parrot.auth.data;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

class UserRowMapper implements RowMapper<User> {
    @Override
    public final User mapRow(ResultSet rs, int rowNum) throws SQLException {
        var user = new User();
        user.setId(rs.getObject("id", UUID.class));
        user.setRole(Role.valueOf(rs.getString("role")));
        user.setLogin(rs.getString("login"));
        return user;
    }
}
