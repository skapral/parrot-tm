package com.skapral.parrot.tasks.ops;

import com.skapral.parrot.common.Operation;
import io.vavr.Tuple;
import io.vavr.collection.List;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Random;
import java.util.UUID;

public class AssignTasks implements Operation {
    private final JdbcTemplate template;
    private final Random random;
    private final List<UUID> taskIds;

    public AssignTasks(JdbcTemplate template, Random random, List<UUID> taskIds) {
        this.template = template;
        this.random = random;
        this.taskIds = taskIds;
    }

    @Override
    public final void execute() {
        var candidates = List.ofAll(template.queryForList("SELECT id FROM assignees", UUID.class));
        taskIds.map(t -> Tuple.of(t, candidates.get(random.nextInt(candidates.size()))))
                .forEach(tpl -> {
                    var taskId = tpl._1;
                    var assigneeId = tpl._2;
                    template.update("UPDATE task SET assignee = ? WHERE id = ?", assigneeId, taskId);
                });
    }
}
