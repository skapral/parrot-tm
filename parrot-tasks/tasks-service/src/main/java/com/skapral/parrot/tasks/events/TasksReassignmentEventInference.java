package com.skapral.parrot.tasks.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pragmaticobjects.oo.inference.api.Inference;
import com.pragmaticobjects.oo.inference.api.Infers;
import com.skapral.parrot.common.Event;
import com.skapral.parrot.common.EventInferred;
import com.skapral.parrot.common.events.EventType;
import com.skapral.parrot.common.events.data.TaskAssignment;
import com.skapral.parrot.common.events.data.TaskAssignments;
import com.skapral.parrot.common.events.impl.NoEvent;
import com.skapral.parrot.common.events.impl.RabbitEvent;
import io.vavr.collection.List;
import org.springframework.jdbc.core.JdbcTemplate;

public @Infers(value = "TasksReassignmentEvent", using = EventInferred.class) class TasksReassignmentEventInference implements Inference<Event> {
    private final JdbcTemplate template;
    private final ObjectMapper objectMapper;
    private final String exchange;
    private final String routingKey;
    private final List<TaskAssignment> taskAssignments;

    public TasksReassignmentEventInference(JdbcTemplate template, ObjectMapper objectMapper, String exchange, String routingKey, List<TaskAssignment> taskAssignments) {
        this.template = template;
        this.objectMapper = objectMapper;
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
            template,
            objectMapper,
            exchange,
            routingKey,
            EventType.TASKS_REASSIGNED,
            new TaskAssignments(
                taskAssignments.map(ta -> new TaskAssignment(ta.getAssigneeId(), ta.getTaskId()))
            )
        );
    }
}
