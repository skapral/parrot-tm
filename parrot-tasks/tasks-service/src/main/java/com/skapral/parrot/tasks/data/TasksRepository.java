package com.skapral.parrot.tasks.data;


import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface TasksRepository extends CrudRepository<Task, UUID> {
}
