package com.skapral.parrot.tasks.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pragmaticobjects.oo.memoized.chm.MemoryCHM;
import com.skapral.parrot.common.DoAndNotify;
import com.skapral.parrot.common.SequentialOperation;
import com.skapral.parrot.common.events.EventType;
import com.skapral.parrot.common.events.data.TaskAssignment;
import com.skapral.parrot.common.events.data.TaskAssignments;
import com.skapral.parrot.common.events.impl.MultipleEvents;
import com.skapral.parrot.common.events.impl.RabbitEvent;
import com.skapral.parrot.tasks.events.TasksReassignmentEvent;
import com.skapral.parrot.tasks.ops.CompleteTask;
import com.skapral.parrot.tasks.ops.CreateTask;
import com.skapral.parrot.tasks.ops.DoTaskAssignments;
import com.skapral.parrot.tasks.queries.*;
import io.vavr.collection.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.UUID;

@RestController
@Transactional
public class TasksRest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Random random;

    @GetMapping
    public Iterable<Task> tasks() {
        return new Tasks(jdbcTemplate).get();
    }

    @PostMapping
    @Secured("MANAGER")
    public void newTask(@RequestParam("description") String description) {
        var memory = new com.pragmaticobjects.oo.memoized.chm.MemoryCHM();
        var taskId = UUID.randomUUID();
        var taskAssignments = new RandomTasksAssignments(
            memory,
            List.of(taskId),
            new IdsOfAllPossibleAssignees(jdbcTemplate).get(),
            random
        ).get();
        new DoAndNotify(
            new SequentialOperation(
                new CreateTask(
                    jdbcTemplate,
                    taskId,
                    description
                ),
                new DoTaskAssignments(
                    jdbcTemplate,
                    taskAssignments
                )
            ),
            new MultipleEvents(
                new RabbitEvent<>(
                    jdbcTemplate,
                    objectMapper,
                    "outbox",
                    "",
                    EventType.TASK_NEW,
                    new com.skapral.parrot.common.events.data.Task(taskId)
                ),
                new TasksReassignmentEvent(
                    jdbcTemplate,
                    objectMapper,
                    "outbox",
                    "",
                    taskAssignments.map(ta -> new TaskAssignment(ta.getAssigneeId(), ta.getTaskId()))
                )
            )
        ).execute();
    }

    @PostMapping("close")
    public void closeTask(@RequestParam("id") UUID id) {
        new DoAndNotify(
            new CompleteTask(
                jdbcTemplate,
                id
            ),
            new RabbitEvent<>(
                jdbcTemplate,
                objectMapper,
                "outbox",
                "",
                EventType.TASK_COMPLETED,
                new TaskAssignments(
                    List.of(
                        new com.skapral.parrot.common.events.data.TaskAssignment(
                            new AssigneeIdByTask(jdbcTemplate, id).get(),
                            id
                        )
                    )
                )
            )
        ).execute();
    }

    @PostMapping("assign")
    @Secured("MANAGER")
    public void assign() {
        var memory = new MemoryCHM();
        var taskIds = new IdsOfTasksInProgress(jdbcTemplate).get();
        var taskAssignments = new RandomTasksAssignments(
            memory,
            taskIds,
            new IdsOfAllPossibleAssignees(jdbcTemplate).get(),
            random
        ).get();
        new DoAndNotify(
            new DoTaskAssignments(
                jdbcTemplate,
                taskAssignments
            ),
            new TasksReassignmentEvent(
                jdbcTemplate,
                objectMapper,
                "outbox",
                "",
                taskAssignments.map(ta -> new TaskAssignment(ta.getAssigneeId(), ta.getTaskId()))
            )
        ).execute();
    }
}
