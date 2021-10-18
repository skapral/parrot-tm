package com.skapral.parrot.tasks.data;


import org.springframework.data.repository.CrudRepository;

public interface TasksRepository extends CrudRepository<Task, Integer> {
}
