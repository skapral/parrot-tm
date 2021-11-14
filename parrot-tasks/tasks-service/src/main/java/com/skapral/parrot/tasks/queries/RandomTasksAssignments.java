package com.skapral.parrot.tasks.queries;

import com.pragmaticobjects.oo.memoized.core.MemoizedCallable;
import com.pragmaticobjects.oo.memoized.core.Memory;
import com.skapral.parrot.common.Query;
import com.skapral.parrot.tasks.rest.TaskAssignment;
import io.vavr.Tuple;
import io.vavr.collection.List;

import java.util.Random;
import java.util.UUID;

public class RandomTasksAssignments implements Query<List<TaskAssignment>> {
    private final Memory memory;
    private final List<UUID> taskIds, assigneeIds;
    private final Random random;

    public RandomTasksAssignments(Memory memory, List<UUID> taskIds, List<UUID> assigneeIds, Random random) {
        this.memory = memory;
        this.taskIds = taskIds;
        this.assigneeIds = assigneeIds;
        this.random = random;
    }

    @Override
    public final List<TaskAssignment> get() {
        return memory.memoized(
            new MemoizedCallable<>() {
                @Override
                public final List<TaskAssignment> call() {
                    if(assigneeIds.isEmpty()) {
                        return List.empty();
                    }
                    return taskIds.map(t -> Tuple.of(t, assigneeIds.get(random.nextInt(assigneeIds.size()))))
                        .map(tpl -> {
                            var taskId = tpl._1;
                            var assigneeId = tpl._2;
                            return new TaskAssignment(taskId, assigneeId);
                        });
                }
            }
        );
    }
}
