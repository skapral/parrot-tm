package com.skapral.parrot.accounting.ops;

import com.skapral.parrot.accounting.data.AccountsRepository;
import com.skapral.parrot.accounting.data.RewardRepository;
import com.skapral.parrot.accounting.data.TransactionLog;
import com.skapral.parrot.accounting.data.TransactionLogRepository;

public class ChargeFeeForTaskAssignment implements Operation {
    private final AccountsRepository accounts;
    private final TransactionLogRepository transactionLogs;
    private final RewardRepository rewards;
    private final Integer taskId;
    private final String taskDescription;
    private final Integer assigneeId;

    public ChargeFeeForTaskAssignment(AccountsRepository accounts, TransactionLogRepository transactionLogs, RewardRepository rewards, Integer taskId, String taskDescription, Integer assigneeId) {
        this.accounts = accounts;
        this.transactionLogs = transactionLogs;
        this.rewards = rewards;
        this.taskId = taskId;
        this.taskDescription = taskDescription;
        this.assigneeId = assigneeId;
    }

    @Override
    public final void execute() {
        if(accounts.existsById(assigneeId)) {
            var reward = rewards.findById(taskId).orElseThrow(() -> new RuntimeException("No such task " + taskId));
            var transaction = new TransactionLog();
            transaction.setAccountId(assigneeId);
            transaction.setDescription(taskDescription);
            transaction.setValue(reward.getPenalty());
            transactionLogs.save(transaction);
        } else {
            throw new RuntimeException("No such account " + assigneeId);
        }
    }
}
