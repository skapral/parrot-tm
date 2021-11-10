package com.skapral.parrot.tasks.events;

import com.pragmaticobjects.oo.inference.api.Inference;
import com.pragmaticobjects.oo.inference.api.Infers;
import com.skapral.parrot.common.Event;
import com.skapral.parrot.common.EventInferred;
import com.skapral.parrot.common.events.EventType;
import com.skapral.parrot.common.events.data.TaskAssignment;
import com.skapral.parrot.common.events.impl.NoEvent;
import com.skapral.parrot.common.events.impl.RabbitEvent;
import io.vavr.collection.List;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

public @Infers(value = "TasksReassignmentEvent", using = EventInferred.class) class TasksReassignmentEventInference implements Inference<Event> {
    private final RabbitTemplate rabbitTemplate;
    private final String exchange;
    private final String routingKey;
    private final List<TaskAssignment> taskAssignments;

    public TasksReassignmentEventInference(RabbitTemplate rabbitTemplate, String exchange, String routingKey, List<TaskAssignment> taskAssignments) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
        this.routingKey = routingKey;
        this.taskAssignments = taskAssignments;
    }

    @Override
    public final Event inferredInstance() {
        if(taskAssignments.isEmpty()) {
            return new NoEvent();
        }
        return new RabbitEvent<>(
            rabbitTemplate,
            exchange,
            routingKey,
            EventType.TASKS_REASSIGNED,
            taskAssignments.map(ta -> new TaskAssignment(ta.getAssigneeId(), ta.getTaskId()))
        );
    }
}
