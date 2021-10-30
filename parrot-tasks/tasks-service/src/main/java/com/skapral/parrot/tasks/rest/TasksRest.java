package com.skapral.parrot.tasks.rest;

import com.skapral.parrot.common.DoAndNotify;
import com.skapral.parrot.common.Event;
import com.skapral.parrot.common.events.EventType;
import com.skapral.parrot.common.events.impl.RabbitEvent;
import com.skapral.parrot.tasks.data.Task;
import com.skapral.parrot.tasks.data.Tasks;
import com.skapral.parrot.tasks.data.TasksInProgress;
import com.skapral.parrot.tasks.ops.AssignTasks;
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
        var tasksInProgress = new TasksInProgress(jdbcTemplate).get();
        new AssignTasks(jdbcTemplate, random, tasksInProgress).execute();
        tasksInProgress.map(t -> new RabbitEvent<>(
                rabbitTemplate,
                "outbox",
                "",
                EventType.TASK_ASSIGNED,
                new com.skapral.parrot.common.events.data.Task(t)
        )).forEach(Event::send);
    }
}
