package com.skapral.parrot.auth.data;

import com.skapral.parrot.common.Query;
import io.vavr.collection.List;
import org.springframework.jdbc.core.JdbcTemplate;

public class Users implements Query<List<User>> {
    private final JdbcTemplate template;

    public Users(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public final List<User> get() {
        return List.ofAll(
                template.query("SELECT id, login, role FROM \"user\"", new UserRowMapper())
        );
    }
}
