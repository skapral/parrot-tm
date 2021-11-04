package com.skapral.parrot.tasks.queries;

import com.skapral.parrot.common.Query;
import io.vavr.collection.List;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.UUID;

public class TasksInProgress implements Query<List<UUID>> {
    private final JdbcTemplate template;

    public TasksInProgress(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public final List<UUID> get() {
        return List.ofAll(template.queryForList("SELECT id FROM tasks WHERE status = 'IN_PROGRESS'", UUID.class));
    }
}
