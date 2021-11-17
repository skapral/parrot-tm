package com.skapral.parrot.tasks.ops;

import com.skapral.parrot.common.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.UUID;

public class CreateAssigneeIfNeeded implements Operation {
    private static final Logger log = LoggerFactory.getLogger(CreateAssigneeIfNeeded.class);

    private final JdbcTemplate template;
    private final UUID id;
    private final String name;
    private final String role;

    public CreateAssigneeIfNeeded(JdbcTemplate template, UUID id, String name, String role) {
        this.template = template;
        this.id = id;
        this.name = name;
        this.role = role;
    }

    @Override
    public final void execute() {
        if("PARROT".equals(role)) {
            template.update("INSERT INTO assignee (id, name) VALUES (?, ?)", id, name);
        } else {
            log.info(name + " is " + role + ": wrong candidate for tasks");
        }
    }
}
