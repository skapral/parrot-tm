package com.skapral.parrot.accounting.ops;

import com.skapral.parrot.common.Operation;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.UUID;

public class ChargeFeeForTaskAssignment implements Operation {
    private final JdbcTemplate template;
    private final UUID taskId;
    private final UUID assigneeId;

    public ChargeFeeForTaskAssignment(JdbcTemplate template, UUID taskId, UUID assigneeId) {
        this.template = template;
        this.taskId = taskId;
        this.assigneeId = assigneeId;
    }

    @Override
    public final void execute() {
        var feeForAssignment = template.queryForObject("SELECT penalty FROM taskcost WHERE taskid = ?", Integer.class, taskId);
        template.update("UPDATE account SET money = money - ? WHERE id = ?", feeForAssignment, assigneeId);
        template.update("INSERT INTO transactionlog (id, accountid, description, credit) values (?, ?, ?, ?)",
                UUID.randomUUID(), assigneeId, "Charged fee for task assignment - " + taskId, feeForAssignment);
    }
}
