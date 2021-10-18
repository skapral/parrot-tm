package com.skapral.parrot.tasks.rest;

import com.skapral.parrot.tasks.data.Status;
import com.skapral.parrot.tasks.data.Task;
import com.skapral.parrot.tasks.data.TasksRepository;
import io.vavr.collection.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@RequestMapping("tasks")
public class TasksRest {
    @Autowired
    public TasksRepository tasksRepo;


    @PostConstruct
    public void init() {
        var tasks = List.of(
                new Task(null, "test task 1", Status.IN_PROGRESS, null),
                new Task(null, "test task 2", Status.IN_PROGRESS, null),
                new Task(null, "test task 3", Status.IN_PROGRESS, null)
        );

        tasksRepo.saveAll(tasks);
    }

    @GetMapping
    public Iterable<Task> tasks() {
        return tasksRepo.findAll();
    }
}
