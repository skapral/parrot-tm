package com.skapral.parrot.tasks.data;

import com.skapral.parrot.common.Query;
import io.vavr.collection.List;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.UUID;

public class AllPossibleAssignees implements Query<List<UUID>> {
    private final JdbcTemplate template;

    public AllPossibleAssignees(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public final List<UUID> get() {
        return List.ofAll(template.queryForList("SELECT id FROM assignees", UUID.class));
    }
}
