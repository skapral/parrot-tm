package com.skapral.parrot.accounting.ops;


import com.skapral.parrot.common.Operation;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.UUID;

public class PayForTaskCompletion implements Operation {
    private final JdbcTemplate template;
    private final UUID taskId;
    private final UUID assigneeId;

    public PayForTaskCompletion(JdbcTemplate template, UUID taskId, UUID assigneeId) {
        this.template = template;
        this.taskId = taskId;
        this.assigneeId = assigneeId;
    }

    @Override
    public final void execute() {
        var rewardForCompletion = template.queryForObject("SELECT reward FROM taskcost WHERE taskid = ?", Integer.class, taskId);
        template.update("UPDATE account SET value = value + ? WHERE id = ?", rewardForCompletion, assigneeId);
        template.update("INSERT INTO transactionlog (id, accountid, description, debit) values (?, ?, ?, ?)",
                UUID.randomUUID(), assigneeId, "Rewarded for task completion - " + taskId, rewardForCompletion);
    }
}
