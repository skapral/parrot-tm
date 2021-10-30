package com.skapral.parrot.tasks.ops;

import com.skapral.parrot.common.Operation;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.UUID;

public class CreateTask implements Operation {
    private final JdbcTemplate template;
    private final UUID id;
    private final String description;

    public CreateTask(JdbcTemplate template, UUID id, String description) {
        this.template = template;
        this.id = id;
        this.description = description;
    }

    @Override
    public final void execute() {
        template.update("INSERT INTO task (id, description) values (?, ?)", id, description);
    }
}
