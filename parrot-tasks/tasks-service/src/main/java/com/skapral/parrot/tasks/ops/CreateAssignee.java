package com.skapral.parrot.tasks.ops;

import org.springframework.jdbc.core.JdbcTemplate;

import java.util.UUID;

public class CreateAssignee implements Operation {
    private final JdbcTemplate template;
    private final UUID id;
    private final String name;

    public CreateAssignee(JdbcTemplate template, UUID id, String name) {
        this.template = template;
        this.id = id;
        this.name = name;
    }

    @Override
    public final void execute() {
        template.update("INSERT INTO assignee (id, name) VALUES (?, ?)", id, name);
    }
}
