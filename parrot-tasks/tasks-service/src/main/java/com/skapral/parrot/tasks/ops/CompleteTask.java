package com.skapral.parrot.tasks.ops;

import com.skapral.parrot.tasks.data.Status;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.UUID;

public class CompleteTask implements Operation {
    private final JdbcTemplate template;
    private final UUID id;

    public CompleteTask(JdbcTemplate template, UUID id) {
        this.template = template;
        this.id = id;
    }

    @Override
    public final void execute() {
        template.update("UPDATE task SET status = ? WHERE id = ?", Status.DONE, id);
    }
}
