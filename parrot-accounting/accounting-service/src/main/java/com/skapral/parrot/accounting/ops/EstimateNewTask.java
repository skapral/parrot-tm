package com.skapral.parrot.accounting.ops;


import com.skapral.parrot.accounting.data.Reward;
import com.skapral.parrot.accounting.data.RewardRepository;

import java.util.Random;

public class EstimateNewTask implements Operation {
    private final RewardRepository rewardRepository;
    private final Integer taskId;
    private final Random estimationSource;

    public EstimateNewTask(RewardRepository rewardRepository, Integer taskId, Random estimationSource) {
        this.rewardRepository = rewardRepository;
        this.taskId = taskId;
        this.estimationSource = estimationSource;
    }

    @Override
    public final void execute() {
        var reward = new Reward();
        reward.setTaskId(taskId);
        reward.setPenalty(-20 + estimationSource.nextInt(10));
        reward.setReward(20 + estimationSource.nextInt(20));
        rewardRepository.save(reward);
    }
}
