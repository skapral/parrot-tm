package com.skapral.parrot.tasks.data;

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
        task.setStatus(rs.getObject("status", Status.class));
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
        return List.ofAll(template.queryForList("select task.id as id, task.description as description, task.assignee as assigneeId, assignee.name as assigneeName, status from task left join assignee on (task.assignee = assignee.id);", Task.class));
    }
}
