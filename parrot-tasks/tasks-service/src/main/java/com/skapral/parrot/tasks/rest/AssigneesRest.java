package com.skapral.parrot.tasks.rest;

import com.skapral.parrot.tasks.data.Assignee;
import com.skapral.parrot.tasks.data.AssigneesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("assignees")
@Transactional
public class AssigneesRest {
    private final AssigneesRepository assigneesRepo;

    @Autowired
    public AssigneesRest(AssigneesRepository assigneesRepo) {
        this.assigneesRepo = assigneesRepo;
    }

    @GetMapping
    public Iterable<Assignee> assignees() {
        return assigneesRepo.findAll();
    }
}
