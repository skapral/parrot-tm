package com.skapral.parrot.tasks.queries;

import com.skapral.parrot.common.Query;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Objects;
import java.util.UUID;

public class AssigneeIdByTask implements Query<UUID> {
    private final JdbcTemplate template;
    private final UUID taskId;

    public AssigneeIdByTask(JdbcTemplate template, UUID taskId) {
        this.template = template;
        this.taskId = taskId;
    }

    @Override
    public final UUID get() {
        return Objects.requireNonNull(
            template.queryForObject("SELECT assignee FROM task WHERE id = ?", UUID.class, taskId)
        );
    }
}
