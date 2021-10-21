package com.skapral.parrot.tasks.rest;

import com.skapral.parrot.tasks.data.AssigneesRepository;
import com.skapral.parrot.tasks.data.Status;
import com.skapral.parrot.tasks.data.Task;
import com.skapral.parrot.tasks.data.TasksRepository;
import io.vavr.collection.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.Random;

@RestController
@RequestMapping("tasks")
@Transactional
public class TasksRest {
    @Autowired
    private TasksRepository tasksRepo;
    @Autowired
    private AssigneesRepository assigneeRepo;


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

    @PostMapping
    public void newTask(@RequestParam("description") String description) {
        var task = new Task();
        task.setDescription(description);
        tasksRepo.save(task);
    }

    @PostMapping
    public void closeTask(@RequestParam("id") Integer id) {
        var optTask = tasksRepo.findById(id);
        optTask.ifPresent(t -> {
            t.setStatus(Status.DONE);
            tasksRepo.save(t);
        });
    }

    @PostMapping("assign")
    public void assign() {
        var random = new Random();
        var tasks = List.ofAll(tasksRepo.findAll());
        var assignees = List.ofAll(assigneeRepo.findAll());
        tasks.forEach(t -> {
            t.setAssignee(assignees.get(random.nextInt(assignees.length())).getId());
        });
        tasksRepo.saveAll(tasks);
    }
}
