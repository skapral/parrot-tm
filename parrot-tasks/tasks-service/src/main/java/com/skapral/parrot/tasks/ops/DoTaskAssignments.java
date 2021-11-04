package com.skapral.parrot.tasks.ops;

import com.skapral.parrot.common.Operation;
import com.skapral.parrot.tasks.rest.TaskAssignment;
import io.vavr.collection.List;
import org.springframework.jdbc.core.JdbcTemplate;

public class DoTaskAssignments implements Operation {
    private final JdbcTemplate template;
    private final List<TaskAssignment> taskAssignments;

    public DoTaskAssignments(JdbcTemplate template, List<TaskAssignment> taskAssignments) {
        this.template = template;
        this.taskAssignments = taskAssignments;
    }

    @Override
    public final void execute() {
        template.batchUpdate(
                "UPDATE task SET assignee = ? WHERE id = ?",
                taskAssignments.map(ta -> new Object[] {ta.getAssigneeId(), ta.getTaskId()}).asJava()
        );
    }
}
