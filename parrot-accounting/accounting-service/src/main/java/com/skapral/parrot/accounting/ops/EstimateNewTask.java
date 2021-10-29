package com.skapral.parrot.accounting.ops;


import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Random;
import java.util.UUID;

public class EstimateNewTask implements Operation {
    private final JdbcTemplate template;
    private final UUID taskId;
    private final Random estimationSource;

    public EstimateNewTask(JdbcTemplate template, UUID taskId, Random estimationSource) {
        this.template = template;
        this.taskId = taskId;
        this.estimationSource = estimationSource;
    }

    @Override
    public final void execute() {
        var penalty = -20 + estimationSource.nextInt(10);
        var reward = 20 + estimationSource.nextInt(20);
        template.update("INSERT INTO reward (taskid, reward, penalty) VALUES (?, ?, ?)", taskId, reward, penalty);
    }
}
