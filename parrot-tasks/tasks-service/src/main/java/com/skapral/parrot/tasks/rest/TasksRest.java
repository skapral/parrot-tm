package com.skapral.parrot.tasks.rest;

import com.skapral.parrot.common.DoAndNotify;
import com.skapral.parrot.common.Event;
import com.skapral.parrot.common.events.EventType;
import com.skapral.parrot.common.events.data.TaskAssignment;
import com.skapral.parrot.common.events.impl.RabbitEvent;
import com.skapral.parrot.tasks.data.*;
import com.skapral.parrot.tasks.ops.DoTaskAssignments;
import com.skapral.parrot.tasks.ops.CompleteTask;
import com.skapral.parrot.tasks.ops.CreateTask;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Random;
import java.util.UUID;

@RestController
@RequestMapping("tasks")
@Transactional
public class TasksRest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Random random;

    @GetMapping
    public Iterable<Task> tasks() {
        return new Tasks(jdbcTemplate).get();
    }

    @PostMapping
    public void newTask(@RequestParam("description") String description) {
        var taskId = UUID.randomUUID();
        new DoAndNotify(
                new CreateTask(
                        jdbcTemplate,
                        taskId,
                        description
                ),
                new RabbitEvent<>(
                        rabbitTemplate,
                        "outbox",
                        "",
                        EventType.TASK_NEW,
                        new com.skapral.parrot.common.events.data.Task(taskId)
                )
        );
    }

    @PostMapping("close")
    public void closeTask(@RequestParam("id") UUID id) {
        new DoAndNotify(
                new CompleteTask(
                        jdbcTemplate,
                        id
                ),
                new RabbitEvent<>(
                        rabbitTemplate,
                        "outbox",
                        "",
                        EventType.TASK_COMPLETED,
                        new com.skapral.parrot.common.events.data.Task(id)
                )
        ).execute();
    }

    @PostMapping("assign")
    public void assign() {
        var taskIds = new TasksInProgress(jdbcTemplate).get();
        var taskAssignments = new RandomTasksAssignments(
                taskIds,
                new AllPossibleAssignees(jdbcTemplate).get(),
                random
        ).get();
        new DoAndNotify(
            new DoTaskAssignments(
                    jdbcTemplate,
                    taskAssignments
            ),
            new RabbitEvent<>(
                    rabbitTemplate,
                    "outbox",
                    "",
                    EventType.TASKS_REASSIGNED,
                    taskAssignments.map(ta -> new TaskAssignment(ta.getAssigneeId(), ta.getTaskId()))
            )
        ).execute();
    }
}
