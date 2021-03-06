package com.skapral.parrot.tasks.queries;

import com.skapral.parrot.common.Query;
import com.skapral.parrot.tasks.rest.Status;
import com.skapral.parrot.tasks.rest.Task;
import io.vavr.collection.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

class TaskRowMapper implements RowMapper<Task> {
    @Override
    public final Task mapRow(ResultSet rs, int rowNum) throws SQLException {
        var task = new Task();
        task.setId(rs.getObject("id", UUID.class));
        task.setDescription(rs.getString("description"));
        task.setStatus(Status.valueOf(rs.getObject("status", String.class)));
        task.setAssigneeId(rs.getObject("assigneeId", UUID.class));
        task.setAssigneeName(rs.getString("assigneeName"));
        return task;
    }
}

public class Tasks implements Query<List<Task>> {
    private final JdbcTemplate template;

    public Tasks(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public final List<Task> get() {
        return List.ofAll(
            template.query("select task.id as id, task.description as description, task.assignee as assigneeId, assignee.name as assigneeName, status from task left join assignee on (task.assignee = assignee.id);", new TaskRowMapper())
        );
    }
}
