package com.skapral.parrot.tasks.rest;

import com.skapral.parrot.tasks.data.Task;
import com.skapral.parrot.tasks.data.Tasks;
import com.skapral.parrot.tasks.ops.AssignAllTasks;
import com.skapral.parrot.tasks.ops.CompleteTask;
import com.skapral.parrot.tasks.ops.ComplexOperation;
import com.skapral.parrot.tasks.ops.CreateTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.Random;
import java.util.UUID;

@RestController
@RequestMapping("tasks")
@Transactional
public class TasksRest {
    @Autowired
    private JdbcTemplate template;

    @Autowired
    private Random random;

    @PostConstruct
    public void init() {
        new ComplexOperation(
                new CreateTask(
                        template,
                        UUID.randomUUID(),
                        "test task 1"
                ),
                new CreateTask(
                        template,
                        UUID.randomUUID(),
                        "test task 2"
                ),
                new CreateTask(
                        template,
                        UUID.randomUUID(),
                        "test task 3"
                )
        ).execute();
    }

    @GetMapping
    public Iterable<Task> tasks() {
        return new Tasks(template).get();
    }

    @PostMapping
    public void newTask(@RequestParam("description") String description) {
        new CreateTask(
                template,
                UUID.randomUUID(),
                description
        ).execute();
    }

    @PostMapping("close")
    public void closeTask(@RequestParam("id") UUID id) {
        new CompleteTask(
                template,
                id
        ).execute();
    }

    @PostMapping("assign")
    public void assign() {
        new AssignAllTasks(template, random).execute();
    }
}
